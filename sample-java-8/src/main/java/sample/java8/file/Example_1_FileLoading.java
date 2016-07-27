package sample.java8.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Example 1:
 *     This example will load file 'module.conf' content line by line and keep it in a List
 * @author kylin
 *
 */
public class Example_1_FileLoading {

    public static void main(String[] args) throws IOException {

        List<String> lines = null;
        Path moduleConf = Paths.get("module.conf");
        if(Files.exists(moduleConf)){
            try (BufferedReader reader = new BufferedReader(new FileReader(moduleConf.toFile()))){
                lines = reader.lines().collect(Collectors.toList());
            }
        }
        System.out.println(lines);
    }

}
