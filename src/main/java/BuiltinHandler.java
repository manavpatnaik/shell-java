import java.nio.file.Paths;
import java.util.*;

public class BuiltinHandler {
    private static Set<String> builtins = new HashSet<>(List.of("type", "echo", "exit", "pwd"));

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
        System.out.println(Paths.get("").toAbsolutePath().toString());
    }
}
