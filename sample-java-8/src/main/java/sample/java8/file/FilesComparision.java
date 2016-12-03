package sample.java8.file;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class FilesComparision {

    public static void main(String[] args) throws IOException {

        Path newBuild = Paths.get("/home/kylin/src/jira/rareddy/build_new/target/teiid-9.1.0.Beta2-SNAPSHOT-wildfly-server/modules");
        final Set<String> newSet = new HashSet<>();
        Set<String> newDirSet = new HashSet<>();
        Files.walkFileTree(newBuild, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                String path = dir.toString();
                if(path.contains("modules/system/layers")){
                    path = path.substring(path.indexOf("modules/system/layers"));
                    newDirSet.add(path);
                }   
                return super.postVisitDirectory(dir, exc);
            }
            
            @Override
            public FileVisitResult visitFile(Path file,  BasicFileAttributes attrs) throws IOException {
                String path = file.toString();
                path = path.substring(path.indexOf("modules/system/layers"));
                newSet.add(path);
                return super.visitFile(file, attrs);
            }});
        
        
        
        Path oldBuild = Paths.get("/home/kylin/tmp/teiid-9.1.0.Beta2-SNAPSHOT/modules/");
        Set<String> oldSet = new HashSet<>();
        Set<String> oldDirSet = new HashSet<>();
        Files.walkFileTree(oldBuild, new SimpleFileVisitor<Path>(){

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                String path = dir.toString();
                path = path.substring(path.indexOf("modules/system/layers"));
                oldDirSet.add(path);
                return super.postVisitDirectory(dir, exc);
            }

            @Override
            public FileVisitResult visitFile(Path file,  BasicFileAttributes attrs) throws IOException {
                String path = file.toString();
                path = path.substring(path.indexOf("modules/system/layers"));
                oldSet.add(path);
                return super.visitFile(file, attrs);
            }});
        
//        System.out.println("newSet size: " + newSet.size());
//        System.out.println("oldSet size: " + oldSet.size());
//        System.out.println("newDirSet size: " + newDirSet.size());
//        System.out.println("oldDirSet size: " + oldDirSet.size());
                
        Set<String> newAddedfileSet = newSet.stream().filter(p -> {
            boolean isExist = oldSet.contains(p);
            if(isExist)
                oldSet.remove(p);
            return !isExist && 
                   !p.contains("translator/salesforce-34") && 
                   !p.contains("resource-adapter/salesforce-34");
        }).collect(toSet());
        
        Set<String> newAddedfolderSet = newDirSet.stream().filter(p -> {
            boolean isExist = oldDirSet.contains(p);
            if(isExist)
                oldDirSet.remove(p);
            return !isExist && !p.contains("translator/salesforce-34");
        }).collect(toSet());
        newAddedfileSet.addAll(newAddedfolderSet);
        
        Set<String> missedJarSet = oldSet.stream().filter(p -> {
            return !p.contains("translator/salesforce/34") && !p.contains("resource-adapter/salesforce-34") && !p.contains("translator/prestodb"); 
        }).collect(toSet());
        missedJarSet.addAll(oldDirSet.stream().filter(p -> !p.contains("translator/salesforce")).collect(toSet()));
        
        System.out.println("New involved files: " + buildSet(newAddedfileSet));
        System.out.println("\nMissed files: " + buildSet(missedJarSet));
    }
    
    static String buildSet(Set<String> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("size = " + items.size());
        sb.append("\n").append("[");
        items.forEach(item -> sb.append("\n").append(item));
        sb.append("\n").append("]");
        return sb.toString();
    }


}
