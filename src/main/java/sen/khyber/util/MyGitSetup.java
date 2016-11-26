package sen.khyber.util;

import java.io.IOException;

public class MyGitSetup {
    
    private final Runtime runtime;
    
    public MyGitSetup() {
        runtime = Runtime.getRuntime();
    }
    
    public void exec(final String command) throws IOException {
        runtime.exec("git " + command);
    }
    
    public void config(final String property, final String value) throws IOException {
        exec("config --global " + property + " " + value);
    }
    
    public static void main(final String[] args) throws Exception {
        final MyGitSetup git = new MyGitSetup();
        git.config("user.name", "Khyber Sen");
        git.config("user.email", "kkysen@gmail.com");
        git.config("push.default", "simple");
        git.config("core.editor", "emacs");
    }
    
}
