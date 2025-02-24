import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BuiltinHandler {
    private static Set<String> builtins = new HashSet<>(List.of("type", "echo", "exit", "pwd", "cd"));
    private static String cwd = Paths.get("").toAbsolutePath().toString();

    public static boolean isBuiltin(String cmd) {
        return builtins.contains(cmd);
    }

    public static List<String> getBuiltins() {
        return new ArrayList<>(builtins);
    }

    public static void handleEcho(String s) {
        System.out.println(s);
    }

    public static void handleType(String command, HashMap<String, String> binaries) {
        if (binaries.containsKey(command)) System.out.println(command + " is " + binaries.get(command));
        else System.out.println(command + ": not found");
    }

    public static void handlePwd() {
        System.out.println(cwd);
    }

    public static void handleCd(String s) {
        if (!s.startsWith("/")) cwd = cwd + "/" + s;
        else cwd = s;
        Path path = Paths.get(cwd);
        if (!Files.isDirectory(path)) System.out.println("cd: " + s + ": No such file or directory");
        else cwd = path.normalize().toString();
    }
}
