import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static HashMap<String, String> BINARIES = new HashMap<>();

    private static String removeFirstWord(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) != ' ') i++;
        if (i == s.length()) return s;
        return s.substring(i+1);
    }

    private static List<String> getSplitList(String path) {
        return Arrays.asList(path.split(":"));
    }

    private static List<String> getDirectoryFiles(String directory) throws IOException {
        if (!Files.isDirectory(Paths.get(directory))) return new ArrayList<>();
        try (Stream<Path> stream = Files.list(Paths.get(directory))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }


    private static void initializeBuiltins() {
        List<String> shellBuiltnins = BuiltinHandler.getBuiltins();
        String builtinInfoText = "a shell builtin";
        for (String command : shellBuiltnins)
            BINARIES.put(command, builtinInfoText);
    }

    private static void initializePath() throws IOException {
        List<String> directories = getSplitList(System.getenv("PATH"));
        for (String directory : directories) {
            List<String> commands = getDirectoryFiles(directory);
            for (String cmd : commands)
                if (!BINARIES.containsKey(cmd)) BINARIES.put(cmd, directory+"/"+cmd);
        }
    }

    public static void main(String[] args) throws Exception {
        initializeBuiltins();
        initializePath();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            String cmd = input.split(" ")[0];
            if (input.equals("exit 0")) {
                scanner.close();
                System.exit(0);
            } else if (BuiltinHandler.isBuiltin(cmd)) {
                String cmdRemoved = removeFirstWord(input);
                if (cmd.equals("echo")) BuiltinHandler.handleEcho(cmdRemoved);
                else if (cmd.equals("type")) BuiltinHandler.handleType(cmdRemoved, BINARIES);
                else if (cmd.equals("pwd")) BuiltinHandler.handlePwd();
                else if (cmd.equals("cd")) BuiltinHandler.handleCd(cmdRemoved);
            } else if (BINARIES.containsKey(cmd)) {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(input.split(" "));
                byte[] output = process.getInputStream().readAllBytes();
                System.out.print(new String(output));
            }
            else System.out.println(input + ": command not found");
        }
    }
}
