package sample.java8.ch7;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreams {

    public static void main(String[] args) {

//        long start = System.currentTimeMillis();
//        long sum = sequentialSum(10_000_000);
//        System.out.println("Result: " + sum + ", spend " + (System.currentTimeMillis() - start) + " milliseconds");
   
        System.out.println("Sequential sum done in: " + measureSumPerf(ParallelStreams::sequentialSum, 10_000_000) + " milliseconds\n");
        
        System.out.println("Iterative sum done in: " + measureSumPerf(ParallelStreams::iterativeSum, 10_000_000) + " milliseconds\n");
        
        System.out.println("Parallel sum done in: " + measureSumPerf(ParallelStreams::parallelSum, 10_000_000) + " milliseconds\n" );
        
        System.out.println("Ranged sum done in: " + measureSumPerf(ParallelStreams::rangedSum, 10_000_000) + " milliseconds\n" );
        
        System.out.println("Parallel range sum done in: " + measureSumPerf(ParallelStreams::parallelRangedSum, 10_000_000) + " milliseconds\n" );
        
        System.out.println("Side effect sum done in: " + measureSumPerf(ParallelStreams::sideEffectSum, 10_000_000) + " milliseconds\n" );
        
        System.out.println("Side effect parallel sum done in: " + measureSumPerf(ParallelStreams::sideEffectParallelSum, 10_000_000) + " milliseconds\n" );
    }
    
    public static long measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result: " + sum);
            if (duration < fastest) 
                fastest = duration;
        }
        return fastest;
    }
    
    public static long iterativeSum(long n) {
        long result = 0;
        for (long i = 0; i <= n; i++) {
            result += i;
        }
        return result;
    }

    
    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1).limit(n).reduce(Long::sum).get();
    }

    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(Long::sum).get();
    }
    
    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1, n).reduce(Long::sum).getAsLong();
    }
    
    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n).parallel().reduce(Long::sum).getAsLong();
    }
    
    public static long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).forEach(accumulator::add);
        return accumulator.total;
    }
    
    public static long sideEffectParallelSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
        return accumulator.total;
    }
    
    public static class Accumulator {
        public long total = 0;

        public void add(long value) {
            total += value;
        }
    }



}
