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

/**
 * 
 * 
 * @author Khyber Sen
 */
public class FileOrganizer {
    
    private static final Predicate<? super Path> TRUE_PATH_PREDICATE = x -> true;
    
    private static Entry<Path, Boolean> moveFileToDirectory(final Path root, final Path file) {
        final Path newPath = Paths.get(root.toString() + "/" + file.getFileName());
        try {
            Files.copy(file, newPath);
            if (!newPath.equals(file)) {
                Files.delete(file);
            }
            return new SimpleEntry<Path, Boolean>(newPath, true);
        } catch (final IOException e) {
            e.printStackTrace();
            return new SimpleEntry<Path, Boolean>(file, true);
        }
    }
    
    public static Stream<Entry<Path, Boolean>> unpackDirectory(final Path root,
            final Predicate<? super Path> fileTest) {
        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("must be a directory");
        }
        try {
            return Files.walk(root)
                    .filter(path -> Files.isRegularFile(path))
                    .filter(fileTest)
                    .map(file -> moveFileToDirectory(root, file));
        } catch (final IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }
    
    public static Stream<Entry<Path, Boolean>> unpackDirectory(final Path dir) {
        return unpackDirectory(dir, TRUE_PATH_PREDICATE);
    }
    
    private static Entry<Path, String> extractGroupName(final Path path,
            final Function<Path, String> groupNameExtractor) {
        return new SimpleEntry<Path, String>(path, groupNameExtractor.apply(path));
    }
    
    private static Map<String, Set<Path>> groupFiles(final Stream<Path> paths,
            final Function<Path, String> groupNameExtractor) {
        final Iterable<Entry<Path, String>> groupNames = paths
                .map(path -> extractGroupName(path, groupNameExtractor))::iterator;
        final Map<String, Set<Path>> groups = new HashMap<>();
        for (final Entry<Path, String> entry : groupNames) {
            final String groupName = entry.getValue();
            if (!groups.containsKey(groupName)) {
                groups.put(groupName, new HashSet<>());
            }
            groups.get(groupName).add(entry.getKey());
        }
        return groups;
    }
    
    private static FileTime getCreationTime(final Path path) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class).creationTime();
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Stream<Entry<Path, Boolean>> moveFileGroup(final Path root,
            final Entry<String, Set<Path>> fileGroup) {
        try {
            final Path newDir = Files.createDirectory(Paths.get(root + "/" + fileGroup.getKey()));
            return fileGroup.getValue()
                    .parallelStream()
                    .map(file -> moveFileToDirectory(newDir, file));
        } catch (final IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }
    
    private static Stream<Entry<Path, Boolean>> moveFileGroups(final Path root,
            final Map<String, Set<Path>> fileGroups) {
        return fileGroups.entrySet()
                .parallelStream()
                .flatMap(fileGroup -> moveFileGroup(root, fileGroup));
    }
    
    public static Stream<Entry<Path, Boolean>> organizeFiles(final Path root,
            final Function<Path, String> groupNameExtractor) throws IOException {
        final Stream<Path> files = Files.list(root)
                .filter(file -> Files.isRegularFile(file));
        final Map<String, Set<Path>> fileGroups = groupFiles(files, groupNameExtractor);
        return moveFileGroups(root, fileGroups);
    }
    
    public static Stream<Entry<Path, Boolean>> organizeFilesByCreationTimes(final Path root,
            final Function<FileTime, String> timeExtractor) throws IOException {
        return organizeFiles(root, timeExtractor.compose(FileOrganizer::getCreationTime));
    }
    
    private static String extractMonth(final FileTime time) {
        final String timeString = time.toString();
        System.out.println(timeString);
        return timeString.substring(0, timeString.indexOf("T") - 3);
    }
    
    public static Stream<Entry<Path, Boolean>> organizeByMonth(final Path root) throws IOException {
        return organizeFilesByCreationTimes(root, FileOrganizer::extractMonth);
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganize(
            final Path root,
            final Predicate<? super Path> fileTest,
            final Function<Path, String> groupNameExtractor)
            throws IOException {
        return Pair.of(unpackDirectory(root, fileTest), organizeFiles(root, groupNameExtractor));
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganize(
            final Path root,
            final Function<Path, String> groupNameExtractor)
            throws IOException {
        return unpackAndOrganize(root, TRUE_PATH_PREDICATE, groupNameExtractor);
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganizeByCreationTimes(
            final Path root,
            final Predicate<? super Path> fileTest,
            final Function<FileTime, String> timeExtractor)
            throws IOException {
        return Pair.of(unpackDirectory(root, fileTest),
                organizeFilesByCreationTimes(root, timeExtractor));
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganizeByCreationTimes(
            final Path root,
            final Function<FileTime, String> timeExtractor)
            throws IOException {
        return unpackAndOrganizeByCreationTimes(root, TRUE_PATH_PREDICATE, timeExtractor);
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganizeByMonth(
            final Path root,
            final Predicate<? super Path> fileTest)
            throws IOException {
        return Pair.of(unpackDirectory(root, fileTest), organizeByMonth(root));
    }
    
    public static Pair<Stream<Entry<Path, Boolean>>, Stream<Entry<Path, Boolean>>> unpackAndOrganizeByMonth(
            final Path root)
            throws IOException {
        return unpackAndOrganizeByMonth(root, TRUE_PATH_PREDICATE);
    }
    
    public static void main(final String[] args) throws Exception {
        final String s = "_______________________________________________________________________________________";
        System.out.println("\n\n" + s + "\n\nStarting next test...\n\n" + s + "\n\n");
        final Path root = Paths.get("C:/Users/kkyse/OneDrive/test file organizer");
        System.out.println("\n\nUnpacking directory...\n\n");
        unpackDirectory(root).forEach(System.out::println);
        System.out.println("\n\nOrganizing by month...\n\n");
        organizeByMonth(root).forEach(System.out::println);
    }
    
}
