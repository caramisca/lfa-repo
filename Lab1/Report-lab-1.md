# Topic: Intro to formal languages. Regular grammars. Finite Automata.
## Course: Formal Languages & Finite Automata
## Author: Caraman Mihai
# Theory

#### A formal grammar is a set of rules used to generate strings in a language. It consists of:

- VN (Non-terminals): Symbols that can be replaced (e.g., S, A, B).
- VT (Terminals): Symbols that appear in the final string (e.g., a, b, c).
- P (Production rules): Transform non-terminals into sequences of terminals and/or non-terminals.
- Start Symbol: The initial non-terminal from which the derivation begins.

#### A finite automaton (FA) is a computational model used to recognize patterns in strings. It consists of:

- Q (States): A finite set of states.
- Σ (Alphabet): A finite set of input symbols.
- δ (Transition function): Defines state changes based on input symbols.
- q₀ (Start state): The state where execution begins.
- F (Final states): Accepting states that determine if a string is valid.

#### Grammar to Finite Automaton Conversion
- Each non-terminal is treated as a state.
- Production rules define transitions.
- Terminal-only transitions lead to final states.

    A finite automaton can verify if a string belongs to a language by following state transitions based on input symbols. If the automaton reaches a final state, the string is accepted.


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
    //variables

    public Grammar(Set<String> VN, Set<String> VT, Map<String, List<String>> P, String S) {
        //basic constructor
    }

    public String generateString() {
        StringBuilder result = new StringBuilder();
        char current = startSymbol;

        while (VN.contains(current)) {
            List<String> productions = P.get(current);
            if (productions == null || productions.isEmpty()) break;
            String selectedProduction = productions.get(new Random().nextInt(productions.size()));
            result.append(selectedProduction.charAt(0));

            if (selectedProduction.length() > 1)
                current = selectedProduction.charAt(1);
            else
                break;
        }
        return result.toString();
    }

    public FiniteAutomaton toFiniteAutomaton() {
       // states, sigma , transition

        for (var entry : P.entrySet()) {
            String fromState = String.valueOf(entry.getKey());
            states.add(fromState);
            transitions.putIfAbsent(fromState, new HashMap<>());

            for (String production : entry.getValue()) {
                char terminal = production.charAt(0);
                String toState = (production.length() > 1) ? String.valueOf(production.charAt(1)) : "FINAL";

                transitions.get(fromState).put(terminal, toState);
                states.add(toState);
                if (!VN.contains(toState.charAt(0))) {
                    finalStates.add(toState);
                }
            }
        }

        return new FiniteAutomaton(states, sigma, transitions, startState, finalStates);
    }
}
```

#### FiniteAutomaton Class
```java
class FiniteAutomaton {
    // variables, terminals, transitions and states

    public FiniteAutomaton(Set<String> states, Set<Character> sigma, Map<String, Map<Character, String>> transitions, String startState, Set<String> finalStates) {
        //contructor
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
        // VN, VT and P rules

        Grammar grammar = new Grammar(VN, VT, P, 'S');

        System.out.println("Generated words:");
        for (int i = 0; i < 5; i++) {
            System.out.println(grammar.generateString());
        }

        FiniteAutomaton fa = grammar.toFiniteAutomaton();
        //checks
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
