package sen.khyber.web.scrape.lit;

import sen.khyber.web.Internet;
import sen.khyber.web.scrape.lit.Lit.Category;

import java.io.IOException;
import java.nio.ByteBuffer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class LitAuthorSerializer {
    
    private static final String BASE_URL = Lit.Url.AUTHOR.getUrl();
    
    private static final int BASE_SERIALIZED_LENGTH = 0
            + Integer.BYTES // uid
            + Short.BYTES // name length
            + Float.BYTES // averageRating
            + Short.BYTES; // numStories
    
    private final int uid;
    private final String url;
    private final int numStories;
    
    private final byte[] bytes;
    
    private final int nameStart;
    private final int nameLength;
    
    private final float averageRating;
    
    private final LitStorySerializer[] stories;
    
    public LitAuthorSerializer(final int uid, int numStories) throws IOException {
        this.uid = uid;
        url = BASE_URL + uid + "&page=submissions";
        bytes = Internet.bytes(url);
        //Files.write(Lit.DOWNLOAD_DIR.resolve("silk.txt"), bytes);
        //bytes = Files.readAllBytes(Lit.DOWNLOAD_DIR.resolve("silk.txt"));
        int i = 0;
        while (bytes[i++] != 'o' || bytes[i++] != '"' || bytes[i++] != '>') {
            //System.out.println(new String(bytes, i, 5));
        }
        //System.out.println(new String(bytes, i - 10, 20));
        nameStart = i;
        while (bytes[++i] != '<') {}
        nameLength = i - nameStart;
        //System.out.println("name: " + new String(bytes, nameStart, nameLength));
        i += 0;
        float totalRating = 0;
        stories = new LitStorySerializer[numStories];
        for (int storyNum = 0; storyNum < numStories; storyNum++) {
            try {
                final LitStorySerializer story = new LitStorySerializer(bytes, i);
                stories[storyNum] = story;
                i = story.i;
                totalRating += story.rating;
            } catch (final StoryNumOutOfBoundsException e) {
                new MissingStoriesException(uid, numStories, storyNum).printStackTrace();
                numStories = storyNum;
                break;
            }
        }
        averageRating = totalRating / numStories;
        this.numStories = numStories;
    }
    
    public int serializedLength() {
        int length = BASE_SERIALIZED_LENGTH + nameLength;
        for (int i = 0; i < numStories; i++) {
            length += stories[i].serializedLength();
        }
        return length;
    }
    
    public ByteBuffer serialize() {
        final ByteBuffer out = ByteBuffer.allocate(serializedLength());
        out.putInt(uid);
        out.putFloat(averageRating);
        out.putShort((short) nameLength);
        out.put(bytes, nameStart, nameLength);
        out.putShort((short) numStories);
        for (int i = 0; i < numStories; i++) {
            stories[i].serialize(out);
        }
        return out;
    }
    
    private static class LitStorySerializer {
        
        private static int byteToInt(final byte[] bytes, final int i) {
            return bytes[i] - '0';
        }
        
        private static int twoBytesToInt(final byte[] bytes, int i) {
            return byteToInt(bytes, i++) * 10 + byteToInt(bytes, i);
        }
        
        /*
         * quot = "
         * apos = ' (not decoded)
         * amp = &
         */
        private static byte[] decodeHtmlCharacterEntities(final byte[] bytes, final int start,
                final int end, final int decodedLength) {
            final byte[] decoded = new byte[decodedLength];
            //System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            //System.out.println(new String(bytes, start, end - start));
            for (int i = start, j = 0; i < end; i++) {
                final byte b = bytes[i];
                if (b != '&') {
                    decoded[j++] = b;
                    continue;
                }
                final byte c = bytes[++i];
                final byte replacement = (byte) (c == 'q' ? '"' : c == 'a' ? '&' : c);
                decoded[j++] = replacement;
                while (bytes[++i] != ';') {}
                //System.out.println(new String(bytes, i, end - i));
            }
            return decoded;
        }
        
        private static final int BASE_SERIALIZED_LENGTH = 0
                + Short.BYTES // url length
                + Short.BYTES // title length
                + Float.BYTES // rating
                + Short.BYTES // description length
                + Byte.BYTES // month
                + Byte.BYTES // day
                + Byte.BYTES // year
                + Byte.BYTES; // category
        
        private final byte[] bytes;
        
        private final int urlStart;
        private final int urlLength;
        
        private final int titleStart;
        private final int titleLength;
        private final byte[] titleBytes;
        
        private final float rating;
        
        private final int descriptionStart;
        private final int descriptionLength;
        private final byte[] descriptionBytes;
        
        private final Category category;
        
        private final byte month;
        private final byte day;
        private final byte year;
        
        private final int i;
        
        public LitStorySerializer(final byte[] bytes, int i) throws StoryNumOutOfBoundsException {
            this.bytes = bytes;
            
            { // url
                final int length = bytes.length - 3;
                while (bytes[i++] != '/' || bytes[i++] != 's' || bytes[i++] != '/') {
                    if (i > length) {
                        throw new StoryNumOutOfBoundsException();
                    }
                }
                urlStart = i;
                while (bytes[++i] != '"') {}
                urlLength = i - urlStart;
            }
            //System.out.println("url: " + new String(bytes, urlStart, urlLength));
            
            //System.out.println(new String(bytes, i, 100));
            //System.out.println((char) bytes[i + 1]);
            { // title
                i += 2;
                if (bytes[i] == '<') {
                    i += 6;
                    //System.out.println("skipped <span>");
                } // skip over <span>
                titleStart = i;
                int entityBytes = 0;
                for (;; i++) {
                    final byte b = bytes[i];
                    if (b == '<') { // no &???; character entities to decode
                        break;
                    } else if (b == '&') {
                        //System.out.println(new String(bytes, i, 10));
                        while (bytes[i++] != ';') {
                            entityBytes++;
                        }
                        //System.out.println(entityBytes);
                    }
                }
                //System.out.println("raw title: " + new String(bytes, titleStart, i - titleStart));
                if (entityBytes == 0) {
                    titleLength = i - titleStart;
                    titleBytes = null;
                } else { // switch to decoding mode
                    final int decodedLength = i - titleStart - entityBytes;
                    //System.out.println("\nstart: " + titleStart + "\nend: " + i);
                    titleBytes = decodeHtmlCharacterEntities(bytes, titleStart, i, decodedLength);
                    titleLength = titleBytes.length;
                }
            }
            //System.out.println("title: " + (titleBytes == null
            //        ? new String(bytes, titleStart, titleLength) : new String(titleBytes)));
            
            { // rating
                while (bytes[i++] != '(') {} // skip until '('
                final int ratingInt = (byteToInt(bytes, i) * 10 + byteToInt(bytes, i + 2)) * 10
                        + byteToInt(bytes, i + 3);
                rating = ratingInt > 500 ? 0 : (float) ratingInt / 100; // if rating contains an 'x', then char conversion to a large int
                i += 5; // skip over rest of rating
            }
            //System.out.println("rating: " + rating);
            
            { // description
                i += 6; // skip over </td>
                while (bytes[i++] != '>') {} // skip over <td...>
                descriptionStart = i;
                //System.out.println(new String(bytes, i, 100));
                //System.out.println(new String(bytes));
                int entityBytes = 0;
                for (;; i++) {
                    final byte b = bytes[i];
                    if (b == '<') {
                        break;
                    } else if (b == '&') {
                        while (bytes[i++] != ';') {
                            entityBytes++;
                        }
                    }
                }
                if (entityBytes == 0) {
                    descriptionLength = i - descriptionStart;
                    descriptionBytes = null;
                } else {
                    final int decodedLength = i - descriptionStart - entityBytes;
                    descriptionBytes = decodeHtmlCharacterEntities(bytes, descriptionStart, i,
                            decodedLength);
                    descriptionLength = descriptionBytes.length;
                }
            }
            //System.out.println("description: " + (descriptionBytes == null
            //        ? new String(bytes, descriptionStart, descriptionLength)
            //        : new String(descriptionBytes)));
            
            { // category
                while (bytes[i++] != '/' || bytes[i++] != 'c' || bytes[i++] != '/') {}
                final int categoryStart = i;
                while (bytes[++i] != '"') {}
                category = Category.fromName(new String(bytes, categoryStart, i - categoryStart));
            }
            //System.out.println("category: " + category);
            
            //System.out.println(new String(bytes, i, 100));
            { // date
                while (bytes[i++] != '/' || bytes[i++] < '0' || bytes[i] > '9') {}
                i -= 4;
                //System.out.println(new String(bytes, i, 10));
                month = (byte) twoBytesToInt(bytes, i);
                i += 3;
                day = (byte) twoBytesToInt(bytes, i);
                i += 3;
                year = (byte) twoBytesToInt(bytes, i);
            }
            //System.out.println("month: " + month);
            //System.out.println("day: " + day);
            //System.out.println("year: " + year);
            //System.out.println("date: " + LocalDate.of(2000 + year, month, day));
            
            this.i = i;
        }
        
        public int serializedLength() {
            return BASE_SERIALIZED_LENGTH + urlLength + titleLength + descriptionLength;
        }
        
        public void serialize(final ByteBuffer out) {
            out.put((byte) category.getId());
            out.put(year);
            out.put(month);
            out.put(day);
            out.putFloat(rating);
            
            out.putShort((short) urlLength);
            final byte[] bytes = this.bytes;
            out.put(bytes, urlStart, urlLength);
            
            out.putShort((short) titleLength);
            if (titleBytes == null) {
                out.put(bytes, titleStart, titleLength);
            } else {
                out.put(titleBytes);
            }
            
            out.putShort((short) descriptionLength);
            if (descriptionBytes == null) {
                out.put(bytes, descriptionStart, descriptionLength);
            } else {
                out.put(descriptionBytes);
            }
        }
        
    }
    
    @RequiredArgsConstructor
    @Getter
    @ToString
    private static final class MissingStoriesException extends Exception {
        
        private static final long serialVersionUID = 1L;
        
        private final int uid;
        private final int numStories;
        private final int actualNumStories;
        
    }
    
    private static final class StoryNumOutOfBoundsException extends Exception {
        
        private static final long serialVersionUID = 1L;
        
        public StoryNumOutOfBoundsException() {}
        
    }
    
    public static void main(final String[] args) throws IOException {
        // 1604806
        // 1817050
        // 1191173 silk
        final LitAuthorSerializer authorSerializer = new LitAuthorSerializer(1191173, 320);
        final ByteBuffer buf = authorSerializer.serialize();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println(buf.position());
        System.out.println(authorSerializer.serializedLength());
        buf.position(0);
        //System.out.println(buf.getInt());
        final LitAuthor author = new LitAuthor(buf);
        System.out.println(author);
        final LitStory story = author.getStories().get(0);
        System.out.println(story);
    }
    
}
