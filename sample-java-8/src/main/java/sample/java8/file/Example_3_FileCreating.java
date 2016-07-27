package sample.java8.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Example_3_FileCreating {

    public static void main(String[] args) throws IOException {

        Path metaInfDir = Paths.get("META-INF");
        Path output = Paths.get(metaInfDir.toString(), "classpath.conf");
        if (!Files.exists(output)) {
            Files.createDirectories(metaInfDir);
        }
        Files.createFile(output);
    }

}
