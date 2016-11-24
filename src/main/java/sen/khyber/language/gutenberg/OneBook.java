package sen.khyber.language.gutenberg;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class OneBook {
    
    private static final String GUTENBERG_DIRECTORY = "C:/Users/kkyse/OneDrive/CS/www.gutenberg.lib.md.us";
    
    private static final byte[] threeNewlines = ("\n\n" + repeat("_", 80) + "\n").getBytes();
    
    private static String repeat(final String s, final int times) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
    
    public static Stream<Path> gutenbergStream(final long maxSize) throws IOException {
        return Files.walk(Paths.get(GUTENBERG_DIRECTORY), 1)
                    .parallel()
                    .filter(path -> path.toString().endsWith(".txt"))
                    .limit(maxSize);
    }
    
    public static void create(final Path oneBookPath) throws IOException {
        final OutputStream out = Files.newOutputStream(oneBookPath);
        final AtomicInteger i = new AtomicInteger();
        gutenbergStream(Long.MAX_VALUE).sequential().map(path -> {
            try {
                System.out.println(i.incrementAndGet());
                return Files.readAllBytes(path);
            } catch (final IOException e) {
                e.printStackTrace();
                return new byte[0];
            }
        }).forEach(bytes -> {
            try {
                out.write(bytes);
                out.write(threeNewlines);
                out.flush();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        out.close();
    }
    
    public static void create(final String path) throws IOException {
        create(Paths.get(path));
    }
    
    public static void main(final String[] args) throws Exception {
        create("C:/Users/kkyse/OneDrive/CS/www.gutenberg.lib.md.us/"
                + "OneBook/OneBookJava.txt");
    }
    
}
