package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String input;
    private final List<Token> tokens = new ArrayList<>();

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        String remaining = input;
        while (!remaining.isEmpty()) {
            boolean matched = false;
            remaining = remaining.trim();
            for (TokenType type : TokenType.values()) {
                if (type == TokenType.EOF) continue;
                Pattern pattern = Pattern.compile("^(" + type.pattern + ")");
                Matcher matcher = pattern.matcher(remaining);
                if (matcher.find()) {
                    String text = matcher.group(1);
                    tokens.add(new Token(type, text));
                    remaining = remaining.substring(text.length());
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                throw new RuntimeException("Unexpected token: " + remaining);
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}
