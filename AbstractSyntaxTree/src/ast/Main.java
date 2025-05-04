package ast;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Token {
    public final TokenType type;
    public final String text;

    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, '%s')", type.name(), text);
    }
}

abstract class ASTNode {}

class ProgramNode extends ASTNode {
    public final List<StatementNode> statements;

    public ProgramNode(List<StatementNode> statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "Program" + statements;
    }
}

class StatementNode extends ASTNode {
    public final Token startCommand;
    public final Token videoIdentifier;
    public final Token equals;
    public final Token filePath;
    public final PipelineNode pipeline;

    public StatementNode(Token startCommand, Token videoIdentifier, Token equals, Token filePath, PipelineNode pipeline) {
        this.startCommand = startCommand;
        this.videoIdentifier = videoIdentifier;
        this.equals = equals;
        this.filePath = filePath;
        this.pipeline = pipeline;
    }

    @Override
    public String toString() {
        return String.format("Statement(%s %s %s %s -> %s)",
                startCommand.text, videoIdentifier.text, equals.text, filePath.text, pipeline);
    }
}

class PipelineNode extends ASTNode {
    public final List<CommandNode> commands;

    public PipelineNode(List<CommandNode> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return commands.toString();
    }
}

class CommandNode extends ASTNode {
    public final Token command;
    public final List<ParameterNode> parameters;

    public CommandNode(Token command, List<ParameterNode> parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return String.format("%s %s", command.text, parameters);
    }
}

class ParameterNode extends ASTNode {
    public final Token parameter;
    public final Token value;

    public ParameterNode(Token parameter, Token value) {
        this.parameter = parameter;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s %s", parameter.text, value.text);
    }
}

// ===================== Parser.java =====================
class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token consume(TokenType expected) {
        Token token = peek();
        if (token.type != expected) {
            throw new RuntimeException("Expected " + expected + " but found " + token);
        }
        pos++;
        return token;
    }

    private boolean match(TokenType expected) {
        if (peek().type == expected) {
            pos++;
            return true;
        }
        return false;
    }

    public ProgramNode parseProgram() {
        List<StatementNode> statements = new ArrayList<>();
        while (peek().type != TokenType.EOF) {
            statements.add(parseStatement());
        }
        return new ProgramNode(statements);
    }

    private StatementNode parseStatement() {
        Token startCmd = consume(TokenType.START_COMMAND);
        Token videoId = consume(TokenType.VIDEO_IDENTIFIER);
        Token eq = consume(TokenType.EQUALS);
        Token filePath = consume(TokenType.FILE_PATH);
        PipelineNode pipeline = null;
        if (match(TokenType.PIPE_LINE)) {
            pipeline = parsePipeline();
        }
        consume(TokenType.SEMICOLON);
        return new StatementNode(startCmd, videoId, eq, filePath, pipeline);
    }

    private PipelineNode parsePipeline() {
        List<CommandNode> commands = new ArrayList<>();
        commands.add(parseCommand());
        while (match(TokenType.PIPE_LINE)) {
            commands.add(parseCommand());
        }
        return new PipelineNode(commands);
    }

    private CommandNode parseCommand() {
        Token cmd = consume(TokenType.COMMAND);
        List<ParameterNode> params = new ArrayList<>();
        while (peek().type == TokenType.PARAMETER) {
            Token param = consume(TokenType.PARAMETER);
            Token value;
            if (peek().type == TokenType.NUMBER) {
                value = consume(TokenType.NUMBER);
            } else if (peek().type == TokenType.FILE_PATH) {
                value = consume(TokenType.FILE_PATH);
            } else {
                throw new RuntimeException("Expected value after parameter but found " + peek());
            }
            params.add(new ParameterNode(param, value));
        }
        return new CommandNode(cmd, params);
    }
}

// ===================== Main.java =====================
public class Main {
    public static void main(String[] args) {
        String input = "imp video = \"input.mp4\" -> cut --x 10 --y 20 -> resize --w 1920 --h 1080;";
        //String input = "imp video = \"in.mp4\";";
        //String input = "imp video = \"in.mp4\" -> cut --x 0 --y 0;";

        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        System.out.println("Tokens:");
        tokens.forEach(System.out::println);

        Parser parser = new Parser(tokens);
        ProgramNode program = parser.parseProgram();
        System.out.println("\nAST:");
        System.out.println(program);
    }
}
