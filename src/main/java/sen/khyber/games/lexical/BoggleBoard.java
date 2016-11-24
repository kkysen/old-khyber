package sen.khyber.games.lexical;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.math3.util.Pair;

import lombok.Setter;

import sen.khyber.language.Language;
import sen.khyber.language.Lexicon;
import sen.khyber.util.linkedMatrix.AdjacentLinkedMatrix;
import sen.khyber.util.linkedMatrix.LinkedMatrix;

public class BoggleBoard extends Board {
    
    @SuppressWarnings("unchecked")
    private static final Class<AdjacentLinkedMatrix<String>> LINKED_MATRIX_TYPE = //
            (Class<AdjacentLinkedMatrix<String>>) (Class<?>) AdjacentLinkedMatrix.class;
    
    private static final int DEFAULT_HEIGHT = 4;
    private static final int DEFAULT_WIDTH = 4;
    
    private static final PointValueMap DEFAULT_POINT_VALUE_MAP = //
            new PointValueCollectionMap(new int[][] {
                { 3, 1 },
                { 4, 1 },
                { 5, 2 },
                { 6, 3 },
                { 7, 5 },
                { 8, 11 }
            });
    
    private static final Lexicon DEFAULT_LANGUAGE = Lexicon.safe(Language.ENGLISH, true);
    
    private static final boolean DEFAULT_CONCURRENCY = false;
    
    private BoggleBoard(final LinkedMatrix<String> board, final PointValueMap pointValueMap,
            final Lexicon language, final boolean isConcurrent) {
        super(board, pointValueMap, language, isConcurrent);
    }
    
    // random based on random supplier
    private BoggleBoard(final int height, final int width,
            final Supplier<String> randomLetterSupplier, final PointValueMap pointValueMap,
            final Lexicon language, final boolean isConcurrent)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        super(height, width, randomLetterSupplier, LINKED_MATRIX_TYPE, pointValueMap, language,
                isConcurrent);
    }
    
    // random based off letter frequency
    private BoggleBoard(final int height, final int width,
            final List<Pair<String, Double>> letterFrequencies, final PointValueMap pointValueMap,
            final Lexicon language, final boolean isConcurrent)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        super(height, width, letterFrequencies, LINKED_MATRIX_TYPE, pointValueMap, language,
                isConcurrent);
    }
    
    private BoggleBoard(final int height, final int width,
            final PointValueMap pointValueMap, final Lexicon language, final boolean isConcurrent)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        super(height, width, LINKED_MATRIX_TYPE, pointValueMap, language, isConcurrent);
    }
    
    private static class Builder {
        
        // TODO missing linked matrix constructor
        
        private @Setter int height = DEFAULT_HEIGHT;
        private @Setter int width = DEFAULT_HEIGHT;
        private @Setter PointValueMap pointValueMap = DEFAULT_POINT_VALUE_MAP;
        private @Setter Lexicon language = DEFAULT_LANGUAGE;
        private @Setter boolean concurrent = DEFAULT_CONCURRENCY;
        
        private @Setter List<Pair<String, Double>> letterFrequencies = null;
        
        private @Setter Supplier<String> randomLetterSupplier = null;
        
        public Builder() {}
        
        public void setSize(final int size) {
            setHeight(size);
            setWidth(size);
        }
        
        private BoggleBoard validate()
                throws InstantiationException, IllegalAccessException, IllegalArgumentException,
                InvocationTargetException, NoSuchMethodException, SecurityException {
            if (randomLetterSupplier != null && letterFrequencies != null) {
                throw new IllegalArgumentException("cannot call both constructors");
            }
            if (randomLetterSupplier != null) {
                return new BoggleBoard(height, width, randomLetterSupplier, pointValueMap, language,
                        concurrent);
            } else if (letterFrequencies != null) {
                return new BoggleBoard(height, width, letterFrequencies, pointValueMap, language,
                        concurrent);
            } else {
                return new BoggleBoard(height, width, pointValueMap, language, concurrent);
            }
        }
        
    }
    
    public static BoggleBoard build(final Consumer<Builder> builderConsumer)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        final Builder builder = new Builder();
        builderConsumer.accept(builder);
        return builder.validate();
    }
    
    public static BoggleBoard build()
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        return build(builder -> {});
    }
    
    public static void main(final String[] args) throws Exception {
        final List<FoundWord> words = new ArrayList<>();
        System.out.println("starting");
        final Board spawner = BoggleBoard.build(b -> {
            b.setSize(10);
        });
        Stream.generate(spawner::spawnRandom).limit(10).parallel().forEach(board -> {
            System.out.println("\n" + board + "\n");
            board.solve();
            System.out.println("solved");
            words.addAll(board.getFoundWords());
        });
        words.sort(Collections.reverseOrder());
        words.forEach(System.out::println);
    }
    
}
