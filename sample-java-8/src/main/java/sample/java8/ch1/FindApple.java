package sample.java8.ch1;

import java.util.ArrayList;
import java.util.List;

public class FindApple {
	
	static class Apple {
		
		private String color;
		
		private int weight;

		public Apple(String color, int weight) {
			this.color = color;
			this.weight = weight;
		}

		public String getColor() {
			return color;
		}

		public int getWeight() {
			return weight;
		}
	}
	
	static List<Apple> inventory = new ArrayList<>();
	
	public static List<Apple> filterGreenApples(List<Apple> inventory){
		List<Apple> results = new ArrayList<>();
		for(int i = 0 ; i < inventory.size() ; i ++) {
		Apple apple = inventory.get(i);
		    if("green".equals(apple.getColor())){
		        results.add(apple);
		    }
		}
		return results;
	}
	
	public static List<Apple> filterWeightApples(List<Apple> inventory){
		List<Apple> results = new ArrayList<>();
		for(int i = 0 ; i < inventory.size() ; i ++) {
		Apple apple = inventory.get(i);
		    if(apple.getWeight() > 150){
		        results.add(apple);
		    }
		}
		return results;
	}

	public static void main(String[] args) {

	}

}
