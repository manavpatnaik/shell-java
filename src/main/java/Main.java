import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static String removeFirstWord(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) != ' ') i++;
        return s.substring(i+1);
    }

    private static List<String> getEnvVars(String path) {
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

    private static Map<String, String> BINARIES = new HashMap<>();

    private static void initializeBuiltins() {
        List<String> shellBuiltnins = new ArrayList<>(List.of("type", "echo", "exit"));
        String builtinInfoText = "a shell builtin";
        for (String command : shellBuiltnins)
            BINARIES.put(command, builtinInfoText);
    }

    private static void initializePath() throws IOException {
        List<String> directories = getEnvVars(System.getenv("PATH"));
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
            }
            if (cmd.equals("echo")) System.out.println(removeFirstWord(input));
            else if (cmd.equals("type")) {
                String command = removeFirstWord(input);
                if (BINARIES.containsKey(command)) System.out.println(command + " is " + BINARIES.get(command));
                else System.out.println(command + ": not found");
            } else if (BINARIES.containsKey(cmd)) {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(input);
                byte[] output = process.getInputStream().readAllBytes();
                System.out.print(new String(output));
            }
            else System.out.println(input + ": command not found");
        }
    }
}
