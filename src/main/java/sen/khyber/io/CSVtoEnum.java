package sen.khyber.io;

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

import sen.khyber.util.AlphabetStream;

public class CSVtoEnum {
    
    private Path path;
    private boolean isQuoted;
    
    private String className;
    private List<List<String>> csv;
    private List<List<String>> fields = new ArrayList<>();
    
    private static final String tab = "    ";
    private static final String enumTemplatePath = "C:/Users/kkyse/OneDrive/CS/Eclipse/git/Khyber/src/sen.khyber.io/enums/EnumTemplate.txt";
    private static final String enumsDestinationPath = "C:/Users/kkyse/OneDrive/CS/Eclipse/git/Khyber/src/sen.khyber.io/enums/";
    
    private static String template;
    
    static {
        try {
            template = MyFiles.read(Paths.get(enumTemplatePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public CSVtoEnum(Path path, boolean isQuoted) {
        this.path = path;
        this.isQuoted = isQuoted;
    }
    
    public CSVtoEnum(Path path) {
        this(path, false);
    }
    
    public CSVtoEnum(String path, boolean isQuoted) {
        this(Paths.get(path), isQuoted);
    }
    
    public CSVtoEnum(String path) {
        this(path, false);
    }
    
    private static String nameToVar(String name) {
        name = name.replace(" ", "_");
        if (! Character.isJavaIdentifierStart(name.charAt(0))) name = "_" + name;
        for (int i = 1; i < name.length(); i++) {
            if (! Character.isJavaIdentifierPart(name.charAt(i))) name.replace(name.charAt(i), '$');
        }
        return name;
    }
    
    private static String nameToConstant(String name) {
        return nameToVar(name).toUpperCase();
    }
    
    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    private static String uncapitalize(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
    
    private static String nameToClass(String name) {
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
        String fileName = path.getFileName().toString();
        int i = fileName.lastIndexOf('.');
        className = nameToClass(fileName.substring(0, i));
        template = template.replace("$className", className);
        template = template.replace("$instanceName", uncapitalize(className));
    }
    
    private void parseCSV() {
        try {
            csv = CSV.read(path, isQuoted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String addGetter(String field) {
        int i = field.indexOf(' ');
        return field.substring(0, i) + " @Getter" + field.substring(i);
    }
    
    private void parseFields() {
        List<String> fields = csv.remove(0);
        for (String field : fields) {
            this.fields.add(Arrays.asList(field.split(" ")));
        }
        Iterable<String> fieldsStream = fields.parallelStream().map(this::addGetter)::iterator;
        template = template.replace("$fields", String.join(";\n" + tab, fieldsStream) + ";");
    }
    
    private static String parseDatum(String datum, String type) {
        switch (type) {
            case "byte" : case "short" : case "int" : case "long" :
            case "float" : case "double" : case "boolean" :
                return datum;
            case "char" :
                if (datum.contains("'")) return datum;
                else return "'" + datum + "'";
            case "String" :
                if (datum.contains("\"")) return datum;
                else return "\"" + datum + "\"";
            default:
                if (datum.contains("new") && datum.contains(type)) return datum;
                else return "new " + type + "(" + datum + ")";
        }
    }
    
    private void parseConstants() {
        StringJoiner constants = new StringJoiner(",\n" + tab, "", ";");
        for (List<String> row : csv) {
            String name = nameToConstant(row.get(0));
            StringJoiner constant = new StringJoiner(", ", name + " (", ")");
            for (int i = 0; i < row.size(); i++) {
                constant.add(parseDatum(row.get(i), 
                    fields.get(i)
                    .get(1)));
            }
            constants.add(constant.toString());
        }
        template = template.replace("$constants", constants.toString());
    }
    
    private void parseConstructor() {
        StringJoiner constructorArgs = new StringJoiner(", ");
        StringJoiner constructorStatements = new StringJoiner(";\n" + tab + tab, "", ";");
        for (List<String> field : fields) {
            String type = field.get(1);
            String var = field.get(2);
            constructorArgs.add(type + " " + var);
            constructorStatements.add("this." + var + " = " + var);
        }
        template = template.replace("$constructorArgs", constructorArgs.toString());
        template = template.replace("$constructorStatements", constructorStatements.toString());
    }
    
    // is this faster than stream reflection
    /*private void parseToString() {
        StringJoiner fieldsToString = new StringJoiner(");\n" + tab + tab + "sj.add(", "sj.add(", ");");
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
    
    public Path save(Path dir) throws IOException {
        String template = toString();
        Path path = Paths.get(dir.toString() + "/" + className + ".java");
        MyFiles.write(path, template);
        return path;
    }
    
    public Path save() throws IOException {
        return save(Paths.get(enumsDestinationPath));
    }
    
    public static void main(String[] args) throws Exception {
        //String path = "C:/Users/kkyse/OneDrive/CS/Eclipse/git/Khyber/src/sen.khyber.io/enums/csv/day.csv";
        
        int NUM_FIELDS = 100; // max num parameters 255;
        int NUM_TYPES = 100;
        long time1 = System.currentTimeMillis();
        String fields = AlphabetStream.upper(NUM_FIELDS)
                                      .map(s -> "private int " + s)
                                      .collect(Collectors.joining(","));
        long time2 = System.currentTimeMillis();
        Random random = new Random();
        long time3 = System.currentTimeMillis();
        String ints = IntStream.range(0, NUM_TYPES)
                               .mapToObj(i -> "Test" + i + "," + 
                                              random.ints(NUM_FIELDS)
                                                    .mapToObj(j -> String.valueOf(j))
                                                    .collect(Collectors.joining(","))
                                         )
                               .collect(Collectors.joining("\n"));
        long time4 = System.currentTimeMillis();
        
        System.out.println("alphabet: " + (time2 - time1));
        System.out.println("random: " + (time4 - time3));
        
        String csv = "private String name," + fields + "\n" + ints;
        MyFiles.write(Paths.get("BigEnum.csv"), csv);
        
        String path = "BigEnum.csv";
        CSVtoEnum day = new CSVtoEnum(path);
        System.out.println(day.save());
        
    }
    
}
