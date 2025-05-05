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
        StringBuilder sb = new StringBuilder("Program\n");
        for (StatementNode stmt : statements) {
            sb.append(stmt.toTreeString("    "));
        }
        return sb.toString();
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

    public String toTreeString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("Statement\n");
        sb.append(indent).append("├── StartCommand: ").append(startCommand.text).append("\n");
        sb.append(indent).append("├── VideoIdentifier: ").append(videoIdentifier.text).append("\n");
        sb.append(indent).append("├── Equals: ").append(equals.text).append("\n");
        sb.append(indent).append("├── FilePath: ").append(filePath.text).append("\n");
        if (pipeline != null) {
            sb.append(indent).append("└── Pipeline\n");
            for (int i = 0; i < pipeline.commands.size(); i++) {
                CommandNode cmd = pipeline.commands.get(i);
                boolean isLast = (i == pipeline.commands.size() - 1);
                sb.append(cmd.toTreeString(indent + (isLast ? "    " : "│   "), isLast));
            }
        }
        return sb.toString();
    }
}

class PipelineNode extends ASTNode {
    public final List<CommandNode> commands;

    public PipelineNode(List<CommandNode> commands) {
        this.commands = commands;
    }
}

class CommandNode extends ASTNode {
    public final Token command;
    public final List<ParameterNode> parameters;

    public CommandNode(Token command, List<ParameterNode> parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    public String toTreeString(String indent, boolean isLast) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append(isLast ? "└── " : "├── ").append("Command: ").append(command.text).append("\n");
        for (int i = 0; i < parameters.size(); i++) {
            ParameterNode param = parameters.get(i);
            boolean isLastParam = (i == parameters.size() - 1);
            sb.append(indent).append(isLast ? "    " : "│   ");
            sb.append(isLastParam ? "└── " : "├── ");
            sb.append("Parameter: ").append(param.parameter.text).append(" ").append(param.value.text).append("\n");
        }
        return sb.toString();
    }
}

class ParameterNode extends ASTNode {
    public final Token parameter;
    public final Token value;

    public ParameterNode(Token parameter, Token value) {
        this.parameter = parameter;
        this.value = value;
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
        //String input = "imp video = \"input.mp4\" -> cut --x 10 --y 20 -> resize --w 1920 --h 1080;";
        //String input = "imp video = \"in.mp4\";";
        //String input = "imp video = \"in.mp4\" -> cut --x 0 --y 0;";
        //String input = "imp video = \"input.mp4\" -> speed --lvl 3 -> resize --w 640 --h 360;";
        String input = "imp video = \"project.mp4\" -> cut --x 0 --y 0 -> resize --w 1920 --h 1080 -> rotate --deg 180 -> fade --lvl 1;";

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
