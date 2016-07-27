package sample.java8.ch1;

import java.io.File;

public class HidenFiles {

	public static void main(String[] args) {

		File[] hiddenFiles = new File(".").listFiles(File::isHidden);
		
		for(File file : hiddenFiles){
			System.out.println(file);
		}
	}

}
