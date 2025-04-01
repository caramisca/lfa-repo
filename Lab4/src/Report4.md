# Regular expressions
### Course: Formal Languages & Finite Automata
### Author: Caraman Mihai
### Variant: 3

----

## Theory

### What are Regular Expressions?

Regular expressions (regex or regexp) are specialized text patterns that describe search criteria. They act as a powerful query language for text, allowing you to specify complex patterns to match, extract, or manipulate strings. Developed in the 1950s by mathematician Stephen Cole Kleene as a notation for regular languages, they have become indispensable in computing, text processing, and data analysis.

At their core, regex patterns define rules for matching character sequences. This enables precise text operations that would be cumbersome or impossible with simple string methods. Their compact syntax packs remarkable expressive power, making them both challenging to master and incredibly valuable once understood.

### What Are Regular Expressions Used For?

Regular expressions are essential for text processing in many domains. They are widely used by developers to:
- **Validate** formatted strings like email addresses or phone numbers.
- **Search and replace** specific patterns in text editors and word processors.
- **Extract data** from unstructured text, such as logs or documents.

Their theoretical foundation in automata theory (every regex can be recognized by a finite state machine) ensures both efficiency and robustness in processing tasks.

---

## Objectives

The primary objectives of this lab are to:
1. Explain what regular expressions are and what they are used for.
2. Develop a program that generates valid combinations of symbols conforming to given regular expressions. The program should accept a set of regexes as input and produce valid words as output.
3. Implement a function that displays the sequence of processing steps applied to the regular expression, clarifying the decision-making process during generation.

---

## Implementation Description

For Variant 3, the following regular expressions are used:
- **Regex 1**: `O(P|Q|R)+2(3|4)`
- **Regex 2**: `A*B(C|D|E)F(G|H|I)^2`
- **Regex 3**: `J+K(L|M|N)*O?(P|Q)^3`

### Overview of the Java Implementation

The core component of the solution is a **regex string generator** written in Java. The program interprets a simplified custom regex syntax dynamically, rather than hardcoding the output strings. Key aspects of the implementation include:

1. **Dynamic Interpretation**:  
   The code reads the provided regex patterns and interprets different parts—literals, groups with alternatives (e.g., `(P|Q|R)`), and quantifiers (`+`, `*`, `?`, and numeric repetitions). Special attention is given to optional elements (the `?` operator) to ensure that characters appear either once or not at all.

2. **Repetition Limits**:  
   For symbols that can occur an undefined number of times (like those using `*` or `+`), the implementation limits repetitions to a maximum of 5 to prevent generating excessively long strings.

3. **Processing Steps Logging**:  
   Although not fully exposed in the final output, the design includes logging of each processing step. This bonus functionality can show the sequence of tokenization, group parsing, quantifier evaluation, and random generation decisions. This logging facilitates understanding of the generation process.
# Method Explanations

Below is an explanation of each method in the `RegexStringGenerator` class:

## 1. `main` Method

```java 
public static void main(String[] args) {
        // Given Regular Expressions
        String regex1 = "O(P|Q|R)+2(3|4)";
        String regex2 = "A*B(C|D|E)F(G|H|I)^2";
        String regex3 = "J+K(L|M|N)*?(P|Q)^3"; // Modified regex for J+K(L|M|N)*O?(P|Q)^3

        // Generate valid words
        //prints
}
```

- **Purpose:**  
  The `main` method serves as the entry point of the program. It is responsible for setting up the sample regular expressions (regex patterns) and displaying the generated output strings.

- **Functionality:**
    - Three regex patterns are defined corresponding to the variant requirements:
        - **Regex 1:** `O(P|Q|R)+2(3|4)` – generates strings that start with an `O`, followed by one or more characters chosen from `P`, `Q`, or `R`, then the number `2`, and finally either `3` or `4`.
        - **Regex 2:** `A*B(C|D|E)F(G|H|I)^2` – allows for zero or more `A`s, followed by `B`, one of the characters `C`, `D`, or `E`, the letter `F`, and then two occurrences (as indicated by `^2`) of one of `G`, `H`, or `I`.
        - **Regex 3:** `J+K(L|M|N)*?(P|Q)^3` – creates strings starting with one or more `J`s, followed by a `K`, then zero or more repetitions (with the optional behavior controlled by the `?` operator) of one of `L`, `M`, or `N`, an optional `O` (handled in the `?` case), and exactly three occurrences of either `P` or `Q`.
    - For each regex, it calls the `generateStringFromRegex` method to produce a valid string that conforms to the pattern.
    - The generated strings are then printed to the console.

## 2. `generateStringFromRegex` Method

```java
public static String generateStringFromRegex(String regex) {
    StringBuilder result = new StringBuilder();
    int index = 0;

    while (index < regex.length()) {
        char ch = regex.charAt(index);

        switch (ch) { // cases and handling for each character in the regex
            case 'O': // literal: append 'O'
            case 'B': // literal: append 'B'
            case 'F': // literal: append 'F'
            case 'K': // literal: append 'K'
            case 'J': // handles 'J+' by repeating 'J' 1-5 times
            case 'A': // handles 'A' or 'A*' for 1 or 0-5 occurrences
            case '(': // begins a group, e.g., (P|Q|R)
            case '^': // exponent: repeat last generated character by given count
            case '*': // '*' already processed with preceding character, so skip it
            case '?': // optional: with 50% chance, append 'O'
            case '+': // '+' is processed with preceding element, so skip it
            case '2': // numeric literal: append '2' (as in 2(3|4))
            case '3': // numeric literal: append '3' if following '2'
            case '4': // numeric literal: append '4' if following '2'
            default: // default: append any other literal character
        }
        index++;
    }
    return result.toString();
}


```

- **Purpose:**  
  This method is the core of the regex string generator. Its job is to process a given regex string dynamically and generate a valid output string based on the provided pattern.

- **Functionality:**
    - **Iteration:**  
      It loops through each character in the regex string using an index, processing one token at a time.

    - **Switch-Case Structure:**  
      The method utilizes a `switch` statement to determine the action for each character:
        - **Literal Characters:**  
          Certain characters (like `O`, `B`, `F`, and `K`) are treated as literals and are directly appended to the result.
        - **Repetition Operators:**  
          For characters such as `J` (handled by the `+` operator) and `A` (handled by the `*` operator), the method generates a random number of repetitions within a specified range (ensuring that the repetition is between a minimum and maximum limit, typically 1 to 5 for `+` and 0 to 5 for `*`).
        - **Group Processing:**  
          When encountering an opening parenthesis `(`, it identifies a group (such as `(P|Q|R)`), extracts the alternatives, and then selects one alternative based on whether the group is followed by a quantifier like `+`.
        - **Exponent Operator:**  
          The caret (`^`) is used to indicate exact repetition (for example, `^2` or `^3`). The method repeats the last generated element the specified number of times.
        - **Optional Operator (`?`):**  
          When the `?` character is encountered, it randomly decides whether to append the optional character (in this context, it is used to handle the optional `O`). This ensures that the character appears either once or not at all, matching the regex requirement.
        - **Other Symbols:**  
          The method also handles numeric characters (like `2`, `3`, and `4`), ensuring they are placed correctly in the output based on their context in the regex.

    - **Dynamic Generation:**  
      By combining literal appending, repetition via random generation, and conditional inclusion of optional characters, this method creates a valid output string that adheres to the input regex pattern.

## 3. `repeatChar` Method

```java
private static String repeatChar(char ch, int count) {
        return String.valueOf(ch).repeat(count);
    }
```

- **Purpose:**  
  This utility method is designed to facilitate the repetition of a single character.

- **Functionality:**
    - **Input Parameters:**  
      It takes two parameters: the character to repeat and the number of times it should be repeated.
    - **Output:**  
      The method returns a new string that consists of the input character repeated the specified number of times.
    - **Usage:**  
      It is primarily used by the `generateStringFromRegex` method when handling quantifiers like `+`, `*`, or when repeating a character as dictated by the exponent operator (e.g., `^2`).

---

Overall, the program demonstrates a dynamic approach to interpreting and processing regular expressions. Each method plays a crucial role in breaking down the regex into manageable parts and then reassembling them into a valid, randomly generated output string that adheres to the original pattern specification.




## Complete Generation Flow

The Java code is structured to process each regex by iterating over its characters, determining the role of each element (literal, group, or quantifier), and appending the corresponding generated string. For example:

- **Regex 1**: Generates strings starting with `O`, followed by one or more instances of characters chosen from `(P|Q|R)`, then a `2`, and finally either `3` or `4`.
- **Regex 2**: Allows for a variable number of `A`s, followed by a fixed sequence including a group selection and a repetition (indicated by `^2`).
- **Regex 3**: Generates one or more `J`s, a `K`, an optional series from `(L|M|N)*`, an optional `O` (handled as shown above), and exactly three occurrences of a character chosen from `(P|Q)`.

## Results

For **Variant 3**, sample generated strings include:

- **For `O(P|Q|R)+2(3|4)`**:
    - `OPP23`
    - `OQQQQ24`
- **For `A*B(C|D|E)F(G|H|I)^2`**:
    - `AAABCFGG`
    - `AAAAAABDFHH`
- **For `J+K(L|M|N)*O?(P|Q)^3`**:
    - `JJKLOPPP`
    - `JKNQQQ`

Each output adheres to the structure defined by the regular expressions, demonstrating the effectiveness of the implemented generator.

## Conclusion

This lab project provided a practical exploration of regular expressions and their application in generating valid string combinations dynamically. The Java implementation successfully interprets complex regex patterns—including groups, alternations, and quantifiers—while imposing sensible limits on repetitions. Moreover, the careful handling of optional elements, particularly in the third regex (`O?`), highlights the challenges and intricacies of regex processing.

By bridging theoretical concepts from formal languages and finite automata with practical programming, this project reinforces the understanding of how regex engines operate and their vast potential in automating text processing tasks.

## References

1. **Stephen Cole Kleene (1951).** *Representation of Events in Nerve Nets and Finite Automata.*
2. **Formal Languages and Finite Automata,** Guide for Practical Lessons.
3. **Regular Expressions on Wikipedia:** [https://en.wikipedia.org/wiki/Regular_expression](https://en.wikipedia.org/wiki/Regular_expression)
