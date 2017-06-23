package sen.khyber.misc;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Future;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

import com.koloboke.collect.map.LongLongMap;
import com.koloboke.collect.map.hash.HashLongLongMaps;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class Misc {
    
    public static void main2(final String[] args) throws Exception {
        final LongLongMap map = HashLongLongMaps.getDefaultFactory().newMutableMap();
        //map.put(1, 2);
        System.out.println(map.getClass().getName());
        //final MutableLHashParallelKVLongLongMap mapImpl;
        
        final PatriciaTrie<String> trie = new PatriciaTrie<>();
        
        System.out
                .println(new ArrayList<>(Arrays.asList(1, 2)).stream().map(i -> i + 1).getClass());
        
        final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");
        System.out.println(LocalDate.parse("2/15/17", DATE_FORMATTER));
        
        final String url = "http://square.github.io/okhttp/";
        //        final InputStream in = new URL(url).openStream();
        //        final Field delegateIs = FilterInputStream.class.getDeclaredField("in");
        //        delegateIs.setAccessible(true);
        //        final ChunkedInputStream is = (ChunkedInputStream) delegateIs.get(in);
        //        final Field delegateIs2 = ChunkedInputStream.class.getDeclaredField("in");
        //        delegateIs2.setAccessible(true);
        //        final InputStream is2 = (InputStream) delegateIs2.get(is);
        //        System.out.println(is2.getClass());
        //
        //        final Socket socket = new Socket(InetAddress.getByName("www.literotica.com"), 80);
        //        System.out.println(socket);
        //        final InputStream input = socket.getInputStream();
        //        System.out.println(input.getClass());
        //        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        //        String line;
        //        while ((line = reader.readLine()) != null) {
        //            System.out.println(line);
        //        }
        final long start = System.nanoTime();
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        final Response response = client.newCall(request).execute();
        //final InputStream okis = response.body().byteStream();
        final byte[] b1 = response.body().bytes();
        final long time = System.nanoTime() - start;
        System.out.println(time / 1e9);
        System.out.println(response.body().byteStream().getClass());
        
        final long start1 = System.nanoTime();
        final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        final Future<org.asynchttpclient.Response> f = asyncHttpClient
                .prepareGet(url).execute();
        final org.asynchttpclient.Response r = f.get();
        System.out.println(r.getClass());
        //final InputStream is = r.getResponseBodyAsStream();
        //System.out.println(is.getClass());
        final byte[] b2 = r.getResponseBodyAsBytes();
        asyncHttpClient.close();
        final long time1 = System.nanoTime() - start1;
        System.out.println(time1 / 1e9);
        
        //Fiber<Void> fiber = new Fiber
    }
    
    public static void main(final String[] args) throws IOException {
        final RandomAccessFile file = new RandomAccessFile("C:/Users/kkyse/Desktop/littemp.java",
                "rw");
        final FileChannel channel = file.getChannel();
        final long length = 1000; // extra just in case
        final MappedByteBuffer in = channel.map(MapMode.READ_ONLY, 0, length);
        file.close();
        System.out.println(in.getClass());
        System.in.read();
    }
    
}
