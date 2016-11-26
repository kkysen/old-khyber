package sen.khyber.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyGitSetup {
    
    private final Runtime runtime;
    
    public MyGitSetup() {
        runtime = Runtime.getRuntime();
    }
    
    private void rawExec(final String command) throws IOException {
        runtime.exec(command);
        //System.out.println(command);
    }
    
    public void exec(final String command) throws IOException {
        rawExec("git " + command);
    }
    
    public void config(final String property, final String value) throws IOException {
        exec("config --global " + property + " " + value);
    }
    
    public void config() throws IOException {
        config("user.name", "Khyber Sen");
        config("user.email", "kkysen@gmail.com");
        config("push.default", "simple");
        config("core.editor", "emacs");
    }
    
    public void add(final String arg) throws IOException {
        exec("add " + arg);
    }
    
    public void commit(final String message) throws IOException {
        exec("commit -m " + '"' + message + '"');
    }
    
    public void push(final String message) throws IOException {
        add("-A");
        commit(message);
        exec("push");
    }
    
    public void makeFile(final String fileName, final String contents) throws IOException {
        rawExec("echo \"" + contents.replace("\"", "\\\"") + "\" > " + fileName);
    }
    
    public String generateSshKeys() throws IOException {
        rawExec("ssh-keygen -t rsa -b 4096 -C kkysen@gmail.com");
        rawExec(""); // specify no password
        return new String(Files.readAllBytes(Paths.get("~/.ssh/id_rsa.pub")));
    }
    
    public void clone(final String url) throws IOException {
        exec("clone " + url);
    }
    
    public static void main(final String[] args) throws Exception {
        final MyGitSetup git = new MyGitSetup();
        final String rsaPubKey = git.generateSshKeys();
        System.out.println(rsaPubKey);
        System.in.read();
        git.clone("git@github.com:kkysen/MKS21X.git");
        git.config();
        git.makeFile("school.txt", "testing from school...");
        git.push("tested from school computer");
    }
    
}
