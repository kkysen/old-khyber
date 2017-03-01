package sen.khyber.misc;

import sen.khyber.web.Internet;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class PbsVideoDownloader {
    
    public static void main(final String[] args) {
        final String urlFront = "http://ga.video.cdn.pbs.org/"
                + "videos/slavery-another-name/"
                + "f4283116-9617-4901-acee-0bb940d33d65/256138/"
                + "hd-1080p-mezzanine-16x9/cf1c955b_sban0000-ep-16x9-hls-400k-";
        final String urlEnd = ".ts";
        final Path dir = Paths.get("C:/Users/kkyse", "Downloads",
                "Slavery_With_Another_Name_Pbs_Video");
        IntStream.rangeClosed(1, 860)
                .parallel()
                .forEach(i -> {
                    final String number = String.valueOf(100000 + i).substring(1);
                    final String url = urlFront + number + urlEnd;
                    System.out.println("downloading " + url);
                    try {
                        Internet.download(url, dir.resolve(number + ".ts"));
                        System.out.println("downloaded " + i);
                    } catch (final IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
    }
    
}
