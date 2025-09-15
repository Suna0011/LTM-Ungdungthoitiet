package tt;

//HistoryManager.java
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class HistoryManager {
 private final Path path;

 public HistoryManager(String filename) {
     this.path = Paths.get(filename);
     try {
         if (!Files.exists(path)) Files.createFile(path);
     } catch (IOException e) {
         System.err.println("Can't create history file: " + e.getMessage());
     }
 }

 public synchronized void append(String line) {
     try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
         bw.write(line);
         bw.newLine();
     } catch (IOException e) {
         System.err.println("Write history error: " + e.getMessage());
     }
 }

 public synchronized List<String> readAll() {
     try {
         return Files.readAllLines(path, StandardCharsets.UTF_8);
     } catch (IOException e) {
         System.err.println("Read history error: " + e.getMessage());
         return List.of();
     }
 }
}