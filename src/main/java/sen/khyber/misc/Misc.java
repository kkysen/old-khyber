package sen.khyber.misc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.collections4.trie.PatriciaTrie;

import com.koloboke.collect.map.LongLongMap;
import com.koloboke.collect.map.hash.HashLongLongMaps;

public class Misc {
    
    public static void main(final String[] args) throws Exception {
        final LongLongMap map = HashLongLongMaps.getDefaultFactory().newMutableMap();
        //map.put(1, 2);
        System.out.println(map.getClass().getName());
        //final MutableLHashParallelKVLongLongMap mapImpl;
        
        final PatriciaTrie<String> trie = new PatriciaTrie<>();
        
        System.out
                .println(new ArrayList<>(Arrays.asList(1, 2)).stream().map(i -> i + 1).getClass());
        
        final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");
        System.out.println(LocalDate.parse("2/15/17", DATE_FORMATTER));
    }
    
}
