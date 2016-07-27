package sample.java8.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicReference;

public class Example_2_FileSearching {

    public static void main(String[] args) throws IOException {

        Path src = Paths.get("src/main/java/sample/java8/file");
        AtomicReference<Path> root = new AtomicReference<>();
        
        Files.walkFileTree(src, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult visitFile(Path file,  BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith("Example_2_FileSearching.java")) {
                    root.set(file);
                }
                return super.visitFile(file, attrs);
            }});
        
        System.out.println(root.get());
        
    }

}
