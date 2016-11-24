package sen.khyber.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

public class FileOrganizer {
    
    private static final Predicate<? super Path> TRUE_PATH_PREDICATE = x -> true;
    
    private static Entry<Path, Boolean> moveFileToDirectory(Path root, Path file) {
        Path newPath = Paths.get(root.toString() + "/" +  file.getFileName());
        try {
            Files.copy(file, newPath);
            if (! newPath.equals(file)) {
                Files.delete(file);
            }
            return new SimpleEntry<Path, Boolean>(newPath, true);
        } catch (IOException e) {
            e.printStackTrace();
            return new SimpleEntry<Path, Boolean>(file, true);
        }
    }
    
    public static Stream<Entry<Path, Boolean>> unpackDirectory(Path root, Predicate<? super Path> fileTest) {
        if (! Files.isDirectory(root)) {
            throw new IllegalArgumentException("must be a directory");
        }
        try {
            return Files.walk(root)
                        .filter(path -> Files.isRegularFile(path))
                        .filter(fileTest)
                        .map(file -> moveFileToDirectory(root, file));
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }
    
    public static Stream<Entry<Path, Boolean>> unpackDirectory(Path dir) {
        return unpackDirectory(dir, TRUE_PATH_PREDICATE);
    }
    
    private static Entry<Path, String> extractGroupName(Path path, Function<Path, String> groupNameExtractor) {
        return new SimpleEntry<Path, String>(path, groupNameExtractor.apply(path));
    }
    
    private static Map<String, Set<Path>> groupFiles(Stream<Path> paths, Function<Path, String> groupNameExtractor) {
        Iterable<Entry<Path, String>> groupNames = paths.map(path -> extractGroupName(path, groupNameExtractor))::iterator;
        Map<String, Set<Path>> groups = new HashMap<>();
        for (Entry<Path, String> entry : groupNames) {
            String groupName = entry.getValue();
            if (! groups.containsKey(groupName)) {
                groups.put(groupName, new HashSet<>());
            }
            groups.get(groupName).add(entry.getKey());
        }
        return groups;
    }
    
    private static FileTime getCreationTime(Path path) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class).creationTime();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Stream<Entry<Path, Boolean>> moveFileGroup(Path root, Entry<String, Set<Path>> fileGroup) {
        try {
            Path newDir = Files.createDirectory(Paths.get(root + "/" + fileGroup.getKey()));
            return fileGroup.getValue()
                                    .parallelStream()
                                    .map(file -> moveFileToDirectory(newDir, file));
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }
    
    private static Stream<Entry<Path, Boolean>> moveFileGroups(Path root, Map<String, Set<Path>> fileGroups) {
        return fileGroups.entrySet()
                         .parallelStream()
                         .flatMap(fileGroup -> moveFileGroup(root, fileGroup));
    }
    
    public static Stream<Entry<Path, Boolean>> organizeFiles(Path root, Function<Path, String> groupNameExtractor) throws IOException {
        Stream<Path> files = Files.list(root)
                                  .filter(file -> Files.isRegularFile(file));
        Map<String, Set<Path>> fileGroups = groupFiles(files, groupNameExtractor);
        return moveFileGroups(root, fileGroups);
    }
    
    public static Stream<Entry<Path, Boolean>> organizeFilesByCreationTimes(Path root, Function<FileTime, String> timeExtractor) throws IOException {
        return organizeFiles(root, timeExtractor.compose(FileOrganizer::getCreationTime));
    }
    
    private static String extractMonth(FileTime time) {
        String timeString = time.toString();
        System.out.println(timeString);
        return timeString.substring(0, timeString.indexOf("T") - 3);
    }
    
    public static Stream<Entry<Path, Boolean>> organizeByMonth(Path root) throws IOException {
        return organizeFilesByCreationTimes(root, FileOrganizer::extractMonth);
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganize(
        Path root, 
        Predicate<? super Path> fileTest, 
        Function<Path, String> groupNameExtractor) 
        throws IOException {
        return Pair.of(unpackDirectory(root, fileTest), organizeFiles(root, groupNameExtractor));
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganize(
        Path root, 
        Function<Path, String> groupNameExtractor) 
        throws IOException {
        return unpackAndOrganize(root, TRUE_PATH_PREDICATE, groupNameExtractor);
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganizeByCreationTimes(
        Path root, 
        Predicate<? super Path> fileTest, 
        Function<FileTime, String> timeExtractor) 
        throws IOException {
        return Pair.of(unpackDirectory(root, fileTest), organizeFilesByCreationTimes(root, timeExtractor));
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganizeByCreationTimes(
        Path root, 
        Function<FileTime, String> timeExtractor) 
        throws IOException {
        return unpackAndOrganizeByCreationTimes(root, TRUE_PATH_PREDICATE, timeExtractor);
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganizeByMonth(
        Path root, 
        Predicate<? super Path> fileTest) 
        throws IOException {
        return Pair.of(unpackDirectory(root, fileTest), organizeByMonth(root));
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganizeByMonth(
        Path root) 
        throws IOException {
        return unpackAndOrganizeByMonth(root, TRUE_PATH_PREDICATE);
    }
    
    public static void main(String[] args) throws Exception {
        final String s = "_______________________________________________________________________________________";
        System.out.println("\n\n" + s + "\n\nStarting next test...\n\n" + s + "\n\n");
        Path root = Paths.get("C:/Users/kkyse/OneDrive/test file organizer");
        System.out.println("\n\nUnpacking directory...\n\n");
        unpackDirectory(root).forEach(System.out::println);
        System.out.println("\n\nOrganizing by month...\n\n");
        organizeByMonth(root).forEach(System.out::println);
    }
    
}
