package laboratory3;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a video editing script snippet (Ex: cut, fade, overlay, trim, speed, reverse):");
        String input = scanner.nextLine();
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        tokens.forEach(System.out::println);
    }
}
