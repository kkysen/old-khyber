package sen.khyber.io;

import sen.khyber.util.AlphabetStream;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class CSVtoEnum {
    
    private static final String TAB = "    ";
    private static final String ENUM_PATH = "C:/Users/kkyse/OneDrive/CS/Eclipse/workspace/khyber/src/main/java/sen/khyber/io/enums/";
    private static final String ENUM_TEMPLATE_PATH = ENUM_PATH + "EnumTemplate.txt";
    private static final String ENUM_DESTINATION_PATH = ENUM_PATH;
    private static final String ENUM_SOURCE_PATH = ENUM_PATH + "csv/";
    
    private String template = MyFilesNoExceptions.read(Paths.get(ENUM_TEMPLATE_PATH));
    
    private final Path path;
    private final boolean isQuoted;
    
    private String className;
    private List<List<String>> csv;
    private final List<List<String>> fields = new ArrayList<>();
    
    public CSVtoEnum(final Path path, final boolean isQuoted) {
        this.path = path;
        this.isQuoted = isQuoted;
    }
    
    public CSVtoEnum(final Path path) {
        this(path, false);
    }
    
    public CSVtoEnum(final String path, final boolean isQuoted) {
        this(Paths.get(path), isQuoted);
    }
    
    public CSVtoEnum(final String path) {
        this(path, false);
    }
    
    private static String nameToVar(String name) {
        name = name.replace(" ", "_");
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            name = "_" + name;
        }
        for (int i = 1; i < name.length(); i++) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                name.replace(name.charAt(i), '$');
            }
        }
        return name;
    }
    
    private static String nameToConstant(final String name) {
        return nameToVar(name).toUpperCase();
    }
    
    private static String capitalize(final String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    private static String uncapitalize(final String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
    
    private static String nameToClass(final String name) {
        return capitalize(name);
    }
    
    /*
     * template fields to fill:
     * $className
     * $instanceName
     * $constants
     * $fields
     * $constructorArgs
     * $constructorStatements
     */
    
    private void parseName() {
        final String fileName = path.getFileName().toString();
        final int i = fileName.lastIndexOf('.');
        className = nameToClass(fileName.substring(0, i));
        template = template.replace("$className", className);
        template = template.replace("$instanceName", uncapitalize(className));
    }
    
    private void parseCSV() {
        try {
            csv = CSV.read(path, isQuoted);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    private String addFinalAndGetter(final String field) {
        final int i = field.indexOf(' ');
        return field.substring(0, i) + " final @Getter" + field.substring(i);
    }
    
    private void parseFields() {
        final List<String> fields = csv.remove(0);
        for (final String field : fields) {
            this.fields.add(Arrays.asList(field.split(" ")));
        }
        final Iterable<String> fieldsStream = fields.parallelStream()
                .map(this::addFinalAndGetter)::iterator;
        template = template.replace("$fields", String.join(";\n" + TAB, fieldsStream) + ";");
    }
    
    private static String parseDatum(final String datum, final String type) {
        switch (type) {
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
            case "boolean":
                return datum;
            case "char":
                if (datum.contains("'")) {
                    return datum;
                } else {
                    return "'" + datum + "'";
                }
            case "String":
                if (datum.contains("\"")) {
                    return datum;
                } else {
                    return "\"" + datum + "\"";
                }
            default:
                if (datum.contains("new") && datum.contains(type)) {
                    return datum;
                } else {
                    return "new " + type + "(" + datum + ")";
                }
        }
    }
    
    private void parseConstants() {
        final StringJoiner constants = new StringJoiner(",\n" + TAB, "", ";");
        for (final List<String> row : csv) {
            final String name = nameToConstant(row.get(0));
            final StringJoiner constant = new StringJoiner(", ", name + "(", ")");
            for (int i = 0; i < row.size(); i++) {
                constant.add(parseDatum(row.get(i), fields.get(i).get(1)));
            }
            constants.add(constant.toString());
        }
        template = template.replace("$constants", constants.toString());
    }
    
    private void parseConstructor() {
        // TODO wrap constructorArgs onto many lines so each line is less than 80 characterss
        final StringJoiner constructorArgs = new StringJoiner(", ");
        final StringJoiner constructorStatements = new StringJoiner(";\n" + TAB + TAB, "", ";");
        for (final List<String> field : fields) {
            final String type = field.get(1);
            final String var = field.get(2);
            constructorArgs.add("final " + type + " " + var);
            constructorStatements.add("this." + var + " = " + var);
        }
        template = template.replace("$constructorArgs", constructorArgs.toString());
        template = template.replace("$constructorStatements", constructorStatements.toString());
    }
    
    // is this faster than stream reflection
    /*private void parseToString() {
        StringJoiner fieldsToString = new StringJoiner(");\n" + TAB + TAB + "sj.add(", "sj.add(", ");");
        for (List<String> field : fields) {
            String var = field.get(2);
            fieldsToString.add("\"" + var + ": \" + String.valueOf(" + var + ")");
        }
        template = template.replace("$toString", fieldsToString.toString());
    }*/
    
    // from template
    /*
    import java.util.StringJoiner;
     
    StringJoiner sj = new StringJoiner(", ", "{", "}");
    $toString
    return sj.toString();
    */
    
    @Override
    public String toString() {
        parseName();
        parseCSV();
        parseFields();
        parseConstants();
        parseConstructor();
        //parseToString(); replaced with reflection
        return template;
    }
    
    public Path save(final Path dir) throws IOException {
        final String template = toString();
        final Path path = Paths.get(dir.toString() + "/" + className + ".java");
        MyFiles.write(path, template);
        return path;
    }
    
    public Path save() throws IOException {
        return save(Paths.get(ENUM_DESTINATION_PATH));
    }
    
    public static void bigEnumTest() throws IOException {
        final int NUM_FIELDS = 100; // max num parameters 255;
        final int NUM_TYPES = 100;
        final long time1 = System.currentTimeMillis();
        final String fields = AlphabetStream.upper(NUM_FIELDS)
                .map(s -> "private int " + s)
                .collect(Collectors.joining(","));
        final long time2 = System.currentTimeMillis();
        final Random random = new Random();
        final long time3 = System.currentTimeMillis();
        final String ints = IntStream.range(0, NUM_TYPES)
                .mapToObj(i -> "Test" + i + "," +
                        random.ints(NUM_FIELDS)
                                .mapToObj(j -> String.valueOf(j))
                                .collect(Collectors.joining(",")))
                .collect(Collectors.joining("\n"));
        final long time4 = System.currentTimeMillis();
        
        System.out.println("alphabet: " + (time2 - time1));
        System.out.println("random: " + (time4 - time3));
        
        final String csv = "private String name," + fields + "\n" + ints;
        MyFiles.write(Paths.get("BigEnum.csv"), csv);
        
        final String path = "BigEnum.csv";
        final CSVtoEnum bigEnum = new CSVtoEnum(path);
        System.out.println(bigEnum.save());
    }
    
    public static void run(final String path) throws IOException {
        new CSVtoEnum(ENUM_SOURCE_PATH + path).save();
    }
    
    public static void main(final String[] args) throws Exception {
        bigEnumTest();
        run("day.csv");
        run("month.csv");
    }
    
}
