
# Laboratory Work 3

### Course: Formal Languages & Finite Automata
### Author: Mihai Caraman

---

## Theory
Lexical analysis, often referred to as tokenization, is the process of breaking down a complex string of characters from source code into manageable units known as tokens. These tokens are the basic building blocks that carry meaning, making them crucial for interpreting and processing the input further. The tokens are categorized by regular expressions (regex), each representing a distinct syntactic element of the programming language. Common types of tokens include:
* *keywords* – cut, fade, resize ...
* *identifiers* – video, vid
* *numbers* – 10, 45.6, 100
* *operators* – +, -, *, /, %
* *punctuation* – (, ), {, }, ;

## Objectives:
* Understand the concept of lexical analysis.
* Explore how a lexer functions in tokenizing input.
* Implement a simple lexer and demonstrate its effectiveness.

## Implementation Description
For this lab, I developed a custom lexer designed to process video editing commands. The lexer is built to identify key tokens in a domain-specific language (DSL) for video editing, including commands, operators, numbers, and file paths.

The lexer uses regular expressions (regex) to match different patterns in the input string. These patterns help identify different token types, such as:
- *Commands* like `cut`, `fade`, and `resize`.
- *Identifiers* such as variable names and function calls.
- *Operators* like `+`, `-`, and `*`.
- *Numbers* (both integers and floats).
- *Punctuation* such as parentheses `()`, braces `{}`, and semicolons `;`.

### Lexer Class Implementation
The main component of the lexer is the regular expression that matches these patterns. The `TOKEN_PATTERNS` string contains the regex for matching various token types, including:
- **Numbers** – for identifying integer or float values.
- **Commands** – such as `cut`, `resize`, etc.
- **Identifiers** – for function names or variables.
- **Operators** – for basic arithmetic or comparison operations.
- **Punctuation** – including parentheses and braces.

``` java
public class Lexer{
    private static final String NUMBER_REGEX = "-?\\d+";
    private static final String VIDEO_IDENTIFIER_REGEX = "--(video|vid)";
    private static final String EQUALS_REGEX = "=";
    private static final String FILE_PATH_REGEX = "\"[^\"]+\\.(mp4|avi|mov|flv|mkv)\""; // Specific for video file paths
    private static final String FOLDER_PATH_REGEX = "\"(?!.*\\.(mp4|avi|mov|flv|mkv)$)[^\"]+\"";
    private static final String PARAMETER_REGEX = "--(x|y|w|h|deg|lvl|video|format)\\b";
    private static final String COMMAND_REGEX = "\\b(cut|fade|overlay|trim|speed|reverse|resize|rotate|flipX|flipY)\\b";
    private static final String START_COMMAND_REGEX = "imp";
    private static final String PIPE_LINE_REGEX = "->";
    ...
```

### Token and TokenType Enum

``` java
public enum TokenType {
    COMMAND,         // Command like cut, resize, etc.
    PARAMETER,       // Parameters like --width, --height, --duration, etc.
    NUMBER,          // Numeric values (integers or floats)
    VIDEO_IDENTIFIER, // Identifiers for video processing (--video)
    FILE_PATH,       // File paths, e.g., "input.mp4"
    FOLDER_PATH,     // Generic folder paths
    EQUALS,          // Equals sign (=) for assignments
    START_COMMAND,   // The starting command (e.g., "start")
    PIPE_LINE,       // Represents a pipeline (->)
    QUOTE,           // Quote marks for file paths
    UNKNOWN          // Unrecognized input
}
```

### laboratory4.Main Class 

```java
public class laboratory4.Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a video editing script snippet (Ex: cut, fade, overlay, trim, speed, reverse):");
        String input = scanner.nextLine();
        
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        
        tokens.forEach(System.out::println);
    }
}
```

---

## Results
To showcase the functionality of the lexer, I tested it with various input examples. The following table shows examples of valid and invalid inputs, along with the corresponding results from the lexer.

|                      Valid Input Prompts                      |              Invalid Input Prompts               |
|:-------------------------------------------------------------:|:------------------------------------------------:|
| `cut video --x 10 --y 20 "video.mp4" -> fade --h 500;`         |           `resize --invalidValue 123`            |
| `rotate -> cut`              | `overlay --img "vid.mov" --w 100; trim --h 500;` |

<p align="center">
  <strong>Table 1.</strong> Tokenizer in Action
</p>

As seen in Table 1, the lexer successfully identifies tokens based on the predefined patterns in the regex. The lexer matches keywords, numbers, operators, and punctuation correctly, while it identifies invalid input that doesn’t conform to any recognized pattern.

## Conclusion
I learned the fundamentals of Lexical Analysis and how to implement a basic lexer that processes input into tokens. I also learned how to handle errors when input doesn’t match any token patterns. While the implementation took time, especially with error handling and ensuring comprehensive coverage of edge cases, it was a valuable learning experience to understand how tokenization works in practice.


## References
[1] **A Sample of a Lexer Implementation**  [https://llvm.org/docs/tutorial/MyFirstLanguageFrontend/LangImpl01.html](https://llvm.org/docs/tutorial/MyFirstLanguageFrontend/LangImpl01.html).

[2] **Introduction to Lexical Analysis**  https://en.wikipedia.org/wiki/Lexical_analysis

[3] **What is Lexer Anyway**  https://dev.to/cad97/what-is-a-lexer-anyway-4kdo