import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static String removeFirstWord(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) != ' ') i++;
        return s.substring(i+1);
    }

    private final static Set<String> SHELL_BUILTINS = new HashSet<>(List.of("echo", "exit", "type"));
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage



        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if (input.equals("exit 0")) {
                scanner.close();
                System.exit(0);
            }
            if (input.startsWith("echo ")) System.out.println(removeFirstWord(input));
            else if (input.startsWith("type")) {
                String command = removeFirstWord(input);
                if (SHELL_BUILTINS.contains(command)) System.out.println(command + " is a shell builtin");
                else System.out.println(command + ": not found");
            } else System.out.println(input + ": command not found");
        }
    }
}
