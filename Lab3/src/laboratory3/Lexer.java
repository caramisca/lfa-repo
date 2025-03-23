 package laboratory3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private static final Pattern TOKEN_PATTERNS = Pattern.compile(
            "(?<NUMBER>\\d+(\\.\\d+)?)|" +          // Numbers (integers & floats)
                    "(?<STRING>\"(.*?)\")|" +                // Strings in double quotes
                    "(?<COMMAND>cut|fade|overlay|trim|speed|reverse)|" + // Video editing commands
                    "(?<IDENTIFIER>[a-zA-Z_][a-zA-Z0-9_]*)|" +   // Variables and function names
                    "(?<OPERATOR>[=+\\-*/<>!%])|" +               // Operators (fixed)
                    "(?<LPAREN>\\()|" +                       // Left parenthesis
                    "(?<RPAREN>\\))|" +                       // Right parenthesis
                    "(?<LBRACE>\\{)|" +                       // Left brace
                    "(?<RBRACE>\\})|" +                       // Right brace
                    "(?<SEMICOLON>;)"                          // Semicolon
    );

    private final String input;
    private final Matcher matcher;

    public Lexer(String input) {
        this.input = input;
        this.matcher = TOKEN_PATTERNS.matcher(input);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group("NUMBER") != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group("NUMBER")));
            } else if (matcher.group("STRING") != null) {
                tokens.add(new Token(TokenType.STRING, matcher.group("STRING")));
            } else if (matcher.group("COMMAND") != null) {
                tokens.add(new Token(TokenType.COMMAND, matcher.group("COMMAND")));
            } else if (matcher.group("IDENTIFIER") != null) {
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group("IDENTIFIER")));
            } else if (matcher.group("OPERATOR") != null) {
                tokens.add(new Token(TokenType.OPERATOR, matcher.group("OPERATOR")));
            } else if (matcher.group("LPAREN") != null) {
                tokens.add(new Token(TokenType.LPAREN, "("));
            } else if (matcher.group("RPAREN") != null) {
                tokens.add(new Token(TokenType.RPAREN, ")"));
            } else if (matcher.group("LBRACE") != null) {
                tokens.add(new Token(TokenType.LBRACE, "{"));
            } else if (matcher.group("RBRACE") != null) {
                tokens.add(new Token(TokenType.RBRACE, "}"));
            } else if (matcher.group("SEMICOLON") != null) {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
            }
        }
        tokens.add(new Token(TokenType.EOF, "")); // End of file token
        return tokens;
    }
}