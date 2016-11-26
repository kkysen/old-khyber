package sen.khyber.ai.ml.nlp.ner;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import sen.khyber.io.MyFiles;

/**
 * static utility methods for Stanford's NER (Named Entity Recognition) API
 * 
 * @author Khyber Sen
 */
public class NERs {
    
    private static final String CLASSIFIER_DIRECTORY = "C:/Users/kkyse/OneDrive/"
            + "CS/Eclipse/stanford-ner-2015-12-09/classifiers/";
    
    private static final CRFClassifier<CoreLabel> DEFAULT_CLASSIFIER;
    static {
        DEFAULT_CLASSIFIER = CRFClassifier.getClassifierNoExceptions(CLASSIFIER_DIRECTORY +
                "english.conll.4class.distsim.crf.ser.gz");
    }
    
    private static final List<CRFClassifier<CoreLabel>> CLASSIFIERS;
    static {
        final File classifierDirectory = new File(CLASSIFIER_DIRECTORY);
        final File[] classifierFiles = classifierDirectory.listFiles();
        CLASSIFIERS = new ArrayList<>();
        for (final File file : classifierFiles) {
            if (file.isFile()) {
                final String fileName = file.getAbsolutePath();
                CRFClassifier<CoreLabel> classifier = null;
                try {
                    classifier = CRFClassifier.getClassifierNoExceptions(fileName);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                CLASSIFIERS.add(classifier);
            }
        }
    }
    
    public static List<CRFClassifier<CoreLabel>> getClassifiers() {
        return CLASSIFIERS;
    }
    
    public static Map<String, Set<String>> identifyNers(final String text,
            final CRFClassifier<CoreLabel> classifier) {
        final Map<String, Set<String>> ners = new LinkedHashMap<>();
        final List<List<CoreLabel>> classify = classifier.classify(text);
        for (final List<CoreLabel> coreLabels : classify) {
            for (final CoreLabel coreLabel : coreLabels) {
                final String word = coreLabel.word();
                final String category = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
                if (!category.equals("0")) {
                    if (ners.containsKey(category)) {
                        // key is already their, 
                        // just insert into already existing LinkedHashSet
                        ners.get(category).add(word);
                    } else {
                        final Set<String> temp = new LinkedHashSet<>();
                        temp.add(word);
                        ners.put(category, temp);
                    }
                    //System.out.println(word + ": " + category);
                }
            }
        }
        return ners;
    }
    
    public static Map<String, Set<String>> identifyNers(final String text) {
        return identifyNers(text, DEFAULT_CLASSIFIER);
    }
    
    public static Set<String> identifyPersons(final String text) {
        final Map<String, Set<String>> ners = identifyNers(text);
        final Set<String> persons = ners.getOrDefault("PERSON", new HashSet<>());
        final Set<String> organizations = ners.getOrDefault("ORGANIZATION", new HashSet<>());
        persons.addAll(organizations);
        return persons;
    }
    
    public static void main(final String[] args) throws Exception {
        /*String content = "Under the presidency of George Washington, Alexander "
                + "Hamilton and Thomas Jefferson began to marshall their "
                + "opposing forces into political parties: the Federalist and "
                + "Democratic-Republican Parties.  At the crux of this dichotomy"
                + " lay on one side Hamilton�s belief in a strong national "
                + "government and a fear of extreme democracy and mob rule "
                + "(Spirit 207-208) and on the other side Jefferson�s trust in "
                + "the informed public to ensure democracy and fear of "
                + "corruption in a centralized government (Spirit 207-208).  "
                + "While they were both able to flawlessly extend these "
                + "arguments to all sorts of situations, their validity "
                + "ultimately lies upon their original premises and the observed"
                + " outcomes of their respective policies.  While Hamilton "
                + "wished for an enlightened ruling class to control the "
                + "government as opposed to all the people, this was a result of"
                + " experience, compared to Jefferson�s naive belief that the "
                + "people will be well informed and will create a pure and "
                + "perpetual democracy (Spirit 208).";*/
        //String content = "Ariel Merida Cinderella";
        final String content = MyFiles.read(Paths.get("Les Miserables.txt")).substring(0, 100000);
        //identifyNers(content).entrySet().forEach(System.out::println);
        //identifyPersons(content).forEach(System.out::println);
        for (final CRFClassifier classifier : getClassifiers()) {
            if (classifier != null) {
                System.out.print(classifier);
                identifyNers(content, classifier).entrySet().forEach(System.out::println);
                System.out.println();
            }
        }
    }
    
}
