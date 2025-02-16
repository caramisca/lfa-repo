# Topic: Intro to formal languages. Regular grammars. Finite Automata.
## Course: Formal Languages & Finite Automata
## Author: Caraman Mihai
# Overview
    A formal language can be considered to be the media or the format used to convey information from a sender entity to the one that receives it. The usual components of a language are:

- The alphabet: Set of valid characters;
- The vocabulary: Set of valid words;
- The grammar: Set of rules/constraints over the lang.



# Objectives:
- Implement a class to represent a grammar.

- Generate valid strings from the given grammar.

- Implement functionality to convert the grammar into a finite automaton.

- Check if a given string belongs to the language of the finite automaton.


## Implementation Description
### Grammar Class
The `Grammar` class represents the given grammar with:
- **Non-terminal symbols** (`VN`)
- **Terminal symbols** (`VT`)
- **Production rules** (`P`)
- **Start symbol** (`S`)

A method generates five valid strings based on the production rules.

### FiniteAutomaton Class
The `FiniteAutomaton` class represents the equivalent finite automaton with:
- **States** (`Q`)
- **Alphabet** (`Sigma`)
- **Transition function** (`delta`)
- **Initial state** (`q0`)
- **Final states** (`F`)

A method verifies whether a given string is valid according to the automaton.

### Code Snippets
#### Grammar Class
```java
public class Grammar {
    private Set<String> VN;
    private Set<String> VT;
    private Map<String, List<String>> P;
    private String S;

    public Grammar(Set<String> VN, Set<String> VT, Map<String, List<String>> P, String S) {
        this.VN = VN;
        this.VT = VT;
        this.P = P;
        this.S = S;
    }

    public String generateString() {
        // Generates a valid string
    }

    public FiniteAutomaton toFiniteAutomaton() {
        // Conversion logic to Finite automata
    }
}
```

#### FiniteAutomaton Class
```java
class FiniteAutomaton {
    private Set<String> states;
    private Set<Character> sigma;
    private Map<String, Map<Character, String>> transitions;
    private String startState;
    private Set<String> finalStates;

    public FiniteAutomaton(Set<String> states, Set<Character> sigma, Map<String, Map<Character, String>> transitions, String startState, Set<String> finalStates) {
        this.states = states;
        this.sigma = sigma;
        this.transitions = transitions;
        this.startState = startState;
        this.finalStates = finalStates;
    }

    public boolean stringBelongToLanguage(String input) {
        String currentState = startState;

        for (char symbol : input.toCharArray()) {
            if (!transitions.containsKey(currentState) || !transitions.get(currentState).containsKey(symbol))
                return false;
            currentState = transitions.get(currentState).get(symbol);
        }
        return finalStates.contains(currentState);
    }
}

```

#### Main Execution
```java
public class Main {
    public static void main(String[] args) {
        Set<Character> VN = new HashSet<>(Arrays.asList('S', 'D', 'R'));
        Set<Character> VT = new HashSet<>(Arrays.asList('a', 'b', 'c', 'd', 'f'));
        Map<Character, List<String>> P = new HashMap<>();
        P.put('S', Arrays.asList("aS", "bD", "fR"));
        P.put('D', Arrays.asList("cD", "dR", "d"));
        P.put('R', Arrays.asList("bR", "f"));

        Grammar grammar = new Grammar(VN, VT, P, 'S');

        System.out.println("Generated words:");
        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
        }

        FiniteAutomaton fa = grammar.toFiniteAutomaton();
        System.out.println("\nChecking if words belong to language:");
        System.out.println("abD -> " + fa.stringBelongToLanguage("abD"));
        System.out.println("acf -> " + fa.stringBelongToLanguage("acf"));
        System.out.println("ad -> " + fa.stringBelongToLanguage("ad"));
    }
}
```
## Screenshots
![image](https://github.com/user-attachments/assets/6acfc1eb-b858-4bfd-8b17-0468d2d5b138)

## Results
- Successfully implemented the Grammar class.
- Generated valid strings based on the production rules.
- Converted the grammar into a finite automaton.
- Verified string acceptance using FA state transitions.

## References

- Hopcroft E. and others. Introduction to Automata Theory, Languages and Computation
- LFPC Guide (ELSE)
- Introduction to formal languages (ELSE)
