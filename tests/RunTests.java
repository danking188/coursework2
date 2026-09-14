import java.nio.file.Files;
import java.nio.file.Path;
import jdk.jshell.JShell;
import jdk.jshell.Snippet;

/** Evaluate JShell snippets and fail the process on compilation or runtime errors. */
public class RunTests {
    public static void main(String[] paths) throws Exception {
        try (JShell shell = JShell.create()) {
            shell.eval("import java.util.*;");
            shell.eval("import java.io.*;");
            shell.eval("import java.nio.file.*;");
            for (String path : paths) {
                String remaining = Files.readString(Path.of(path));
                while (!remaining.isBlank()) {
                    var info = shell.sourceCodeAnalysis().analyzeCompletion(remaining);
                    if (info.source() == null || info.source().isBlank()) {
                        throw new AssertionError("Incomplete snippet in " + path);
                    }
                    for (var event : shell.eval(info.source())) {
                        if (event.status() == Snippet.Status.REJECTED || event.exception() != null) {
                            shell.diagnostics(event.snippet()).forEach(d -> System.err.println(d.getMessage(null)));
                            throw new AssertionError(path + ": " + info.source(), event.exception());
                        }
                    }
                    remaining = info.remaining();
                }
            }
            System.out.println("All JShell checks passed (" + paths.length + " files)");
        }
    }
}
