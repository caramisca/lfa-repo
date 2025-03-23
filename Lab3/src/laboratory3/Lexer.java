package laboratory3;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private static final String NUMBER_REGEX = "-?\\d+";
    private static final String VIDEO_IDENTIFIER_REGEX = "video";
    private static final String EQUALS_REGEX = "=";
    private static final String FILE_PATH_REGEX = "\"[^\"]+\\.(mp4|avi|mov|flv|mkv)\""; // Specific for video file paths
    private static final String FOLDER_PATH_REGEX = "\"(?!.*\\.(mp4|avi|mov|flv|mkv)$)[^\"]+\"";
    private static final String PARAMETER_REGEX = "--(x|y|w|h|deg|lvl|video|format)\\b";
    private static final String COMMAND_REGEX = "\\b(cut|fade|overlay|trim|speed|reverse|resize|rotate|flipX|flipY)\\b";
    private static final String START_COMMAND_REGEX = "imp";
    private static final String PIPE_LINE_REGEX = "->";
    private static final String SEMICOLON_REGEX = ";"; // Add regex for semicolon

    private static final String TOKEN_REGEX = String.join("|",
            COMMAND_REGEX, PARAMETER_REGEX, NUMBER_REGEX, VIDEO_IDENTIFIER_REGEX, FILE_PATH_REGEX,
            FOLDER_PATH_REGEX, EQUALS_REGEX, START_COMMAND_REGEX, PIPE_LINE_REGEX, SEMICOLON_REGEX
    );

    private static final Pattern TOKEN_PATTERN = Pattern.compile(TOKEN_REGEX);

    private final String input;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(input);
        int start = 0; // Start index of the current token in the input string

        while (matcher.find()) {
            if (matcher.start() > start) {
                // There's unmatched input before the current match
                String unmatchedInput = input.substring(start, matcher.start()).trim();
                if (!unmatchedInput.trim().isEmpty()) {
                    reportError(unmatchedInput);
                }
            }

            String value = matcher.group();
            TokenType type = determineTokenType(value);

            // Special case for absorbing quotes within the value
            if (type == TokenType.FILE_PATH || type == TokenType.FOLDER_PATH) {
                tokens.add(new Token(TokenType.QUOTE, "\""));
                tokens.add(new Token(type, value.substring(1, value.length() - 1)));
                tokens.add(new Token(TokenType.QUOTE, "\""));
            } else {
                tokens.add(new Token(type, value));
            }

            start = matcher.end();
        }

        // Add EOF token after processing all tokens
        tokens.add(new Token(TokenType.EOF, ""));

        // Handle any trailing unmatched input
        if (start < input.length()) {
            String unmatchedInput = input.substring(start).trim();
            if (!unmatchedInput.trim().isEmpty()) {
                reportError(unmatchedInput); // Handle or report the unmatched input
            }
        }

        return tokens;
    }

    private void reportError(String value) {
        System.err.println("Unrecognized input: '" + value + "'");
    }

    private TokenType determineTokenType(String value) {
        if (value.matches(COMMAND_REGEX)) {
            return TokenType.COMMAND;
        } else if (value.matches(NUMBER_REGEX)) {
            return TokenType.NUMBER;
        } else if (value.matches(VIDEO_IDENTIFIER_REGEX)) {
            return TokenType.VIDEO_IDENTIFIER;
        } else if (value.matches(FILE_PATH_REGEX)) {
            return TokenType.FILE_PATH;
        } else if (value.matches(PARAMETER_REGEX)) {
            return TokenType.PARAMETER;
        } else if (value.matches(FOLDER_PATH_REGEX)) {
            return TokenType.FOLDER_PATH;
        } else if (value.matches(EQUALS_REGEX)) {
            return TokenType.EQUALS;
        } else if (value.matches(START_COMMAND_REGEX)) {
            return TokenType.START_COMMAND;
        } else if (value.matches(PIPE_LINE_REGEX)) {
            return TokenType.PIPE_LINE;
        } else if (value.matches(SEMICOLON_REGEX)) {
            return TokenType.SEMICOLON; // Handle semicolon as a token type
        } else {
            return TokenType.UNKNOWN;
        }
    }
}
