package laboratory3;

public enum TokenType {
    COMMAND,         // Command like cut, resize, etc.
    PARAMETER,       // Parameters like --width, --height, --duration, etc.
    NUMBER,          // Numeric values (integers or floats)
    VIDEO_IDENTIFIER, // Identifiers for video processing (--video)
    FILE_PATH,       // File paths, e.g., "input.mp4"
    FOLDER_PATH,     // Generic folder paths
    EQUALS,          // Equals sign (=) for assignments
    START_COMMAND,
    PIPE_LINE,       // Represents a pipeline (->)
    QUOTE, // Quote marks for file paths
    SEMICOLON,
    EOF,
    UNKNOWN          // Unrecognized input
}
