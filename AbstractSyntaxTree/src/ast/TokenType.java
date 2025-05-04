package ast;

public enum TokenType {
    COMMAND("\\b(cut|fade|overlay|trim|speed|reverse|resize|rotate|flipX|flipY)\\b"),
    PARAMETER("--(x|y|w|h|deg|lvl|video|format)\\b"),
    NUMBER("-?\\d+"),
    VIDEO_IDENTIFIER("video\\b"),
    FILE_PATH("\"[^\"]+\\.(mp4|avi|mov|flv|mkv)\""),
    FOLDER_PATH("\"(?!.*\\.(mp4|avi|mov|flv|mkv)$)[^\"]+\""),
    EQUALS("="),
    START_COMMAND("imp\\b"),
    PIPE_LINE("->"),
    SEMICOLON(";"),
    EOF("");

    public final String pattern;

    TokenType(String pattern) {
        this.pattern = pattern;
    }
}