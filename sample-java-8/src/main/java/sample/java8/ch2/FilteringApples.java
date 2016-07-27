package sample.java8.ch2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilteringApples {
    
    public interface ApplePredicate {
        public boolean predicate(Apple apple);
    }
    
    static class AppleWeightPredicate implements ApplePredicate {
        public boolean predicate(Apple apple) {
            return apple.getWeight() > 150;
        }
    }
    
    static class AppleColorPredicate implements ApplePredicate {
        public boolean predicate(Apple apple) {
            return "green".equals(apple.getColor());
        }
    }
    
    static class AppleRedAndHeavyPredicate implements ApplePredicate {
        public boolean predicate(Apple apple) {
            return "red".equals(apple.getColor()) && apple.getWeight() > 150;
        }
    }
    
    public interface Predicate<T> {
        boolean predicate(T t);
    }
    
    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for(T e : list){
            if(p.predicate(e)){
                result.add(e);
            }
        }
        return result;
    }

	public static void main(String[] args) {
	    
	    List<Apple> inventory = Arrays.asList(new Apple(80,"green"), new Apple(155, "green"), new Apple(120, "red"));
	    
	    // basic attempts
	    List<Apple> greenApples = filterApplesByColor(inventory, "green");
        System.out.println(greenApples);
        
        List<Apple> heavyApples = filterApplesByWeight(inventory, 150);
        System.out.println(heavyApples);
        
        greenApples = filterApples(inventory, "green", 0, true);
        System.out.println(greenApples);
        
        heavyApples = filterApples(inventory, "", 150, false);
        System.out.println(heavyApples);
        
        // Strategy pattern
        greenApples = filterApples(inventory, new AppleColorPredicate());
        System.out.println(greenApples);
        
        heavyApples = filterApples(inventory, new AppleWeightPredicate());
        System.out.println(heavyApples);
        
        // Anonymous classes
        greenApples = filterApples(inventory, new ApplePredicate(){
            public boolean predicate(Apple apple) {
                return "green".equals(apple.getColor());
            }});
        System.out.println(greenApples);
        
        heavyApples = filterApples(inventory, new ApplePredicate(){
            public boolean predicate(Apple apple) {
                return apple.getWeight() > 150;
            }});
        System.out.println(heavyApples);
        
        // lambda expression
        greenApples = filterApples(inventory, (Apple apple) -> "green".equals(apple.getColor()));  
        System.out.println(greenApples);
        
        heavyApples = filterApples(inventory, (Apple apple) -> apple.getWeight() > 150);  
        System.out.println(heavyApples);
        
        // abstracting over List type
        greenApples = filter(inventory, (Apple apple) -> "green".equals(apple.getColor()));  
        System.out.println(greenApples);
        
        heavyApples = filter(inventory, (Apple apple) -> apple.getWeight() > 150);  
        System.out.println(heavyApples);
        
	}
	
	public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
	    List<Apple> result = new ArrayList<>();
	    for(Apple apple: inventory){
	        if(apple.getColor().equals(color)){
	            result.add(apple);
	        }
	    }
	    return result;
    }
	
	public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
	    List<Apple> result = new ArrayList<>();
	    for(Apple apple: inventory){
	        if(apple.getWeight() > weight){
	            result.add(apple);
	        }
	    }
	    return result;
	}
	
	public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean flag){
	    List<Apple> result = new ArrayList<>();
        for(Apple apple: inventory){
            if((flag && apple.getColor().equals(color)) || (!flag && apple.getWeight() > weight)){
                result.add(apple);
            }
        }
        return result;
	}
	
	public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
	    List<Apple> result = new ArrayList<>();
	    for(Apple apple : inventory){
	        if(p.predicate(apple)){
	            result.add(apple);
	        }
	    }
	    return result;
	}

    public static class Apple {
	    
	    private Integer weight = 0;
        private String color = "";
        
        public Apple(Integer weight, String color) {
            this.weight = weight;
            this.color = color;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "Apple{" + "color='" + color + '\'' + ", weight=" + weight + '}';
        }
	}

}
