# Topic: Determinism in Finite Automata. Conversion from NDFA to DFA. Chomsky Hierarchy.

## Course: Formal Languages & Finite Automata
## Author: Caraman Mihai

# Theory

#### A finite automaton (FA) is a computational model used to recognize patterns in strings. It consists of:

- Q (States): A finite set of states.
- Σ (Alphabet): A finite set of input symbols.
- δ (Transition function): Defines state changes based on input symbols.
- q₀ (Start state): The state where execution begins.
- F (Final states): Accepting states that determine if a string is valid.

#### Deterministic vs. Non-Deterministic Finite Automata
- **Deterministic Finite Automaton (DFA):** Each state has exactly one transition for each symbol in the alphabet.
- **Non-Deterministic Finite Automaton (NDFA):** A state can have multiple transitions for the same symbol or ε-moves (empty transitions).


#### NDFA to DFA Conversion
The subset construction algorithm is used to convert an NDFA into an equivalent DFA:
1. Each state in the DFA represents a set of states in the NDFA.
2. Process all transitions from the NDFA to form deterministic transitions.
3. Identify final states as those that contain any accepting state from the NDFA.

#### Chomsky Hierarchy
Grammars are classified into four types:
- **Type 0 (Unrestricted Grammar):** No restrictions on production rules.
- **Type 1 (Context-Sensitive Grammar):** Productions must maintain string length or increase it.
- **Type 2 (Context-Free Grammar):** Each production has a single non-terminal on the left-hand side.
- **Type 3 (Regular Grammar):** Productions follow strict forms (A → aB or A → a).

# Objectives:

- Implement a class to represent a grammar and classify it according to the Chomsky hierarchy.
- Implement functionality to convert an NDFA to a DFA.
- Convert a finite automaton into a regular grammar.
- Check if a given string belongs to the language of the finite automaton.
- Optionally, represent the automaton graphically.

## Implementation Description
### FiniteAutomaton Class
The `FiniteAutomaton` class represents an FA with the following components:

- States (Q)
- Alphabet (Σ)
- Transition function (δ): Represented as a map where each key is a state and each value is another map that associates input symbols to a set of target states.
- Start State (q₀)
- Final States (F)
#### Key methods include:

- isDeterministic(): Checks if there are multiple transitions for any state on the same input symbol.
- toDFA(): Uses the subset construction algorithm to convert an NDFA to an equivalent DFA.
- display(): Outputs the FA’s components for verification.

### Variant 3 Details
For this lab, the finite automaton is defined as follows:

- States: Q = {q0, q1, q2, q3, q4}
- Alphabet: Σ = {a, b}
- Final State: F = {q4}
- Transitions (δ):\
δ(q0, a) = {q1}\
δ(q1, b) = {q1}\
δ(q1, a) = {q2}\
δ(q2, b) = {q2, q3}\
(Note: The nondeterminism appears here with two targets for symbol 'b'.)\
δ(q3, b) = {q4}\
δ(q3, a) = {q1}\
### Main Class
The Main class instantiates the NDFA for Variant 3, displays its properties, converts it to a DFA, and then displays the result. This serves as a client to demonstrate the functionality of the FA conversion.


## Code Snippets

#### FiniteAutomaton Class
```java
import java.util.*;

class FiniteAutomaton {
    // set variables for states, alphabet, transitions, start and final state

    public FiniteAutomaton(Set<String> states, Set<Character> alphabet,
                           Map<String, Map<Character, Set<String>>> transitions,
                           String startState, Set<String> finalStates) {
        //basic constructor
    }

    public boolean isDeterministic() {
        for (var trans : transitions.values()) {
            for (var targets : trans.values())
                if (targets.size() > 1) return false;
        }
        return true;
    }

    // Converts an NDFA to a DFA using the subset construction algorithm.
    public FiniteAutomaton toDFA() {
        Map<Set<String>, String> stateMapping = new HashMap<>();
        Set<String> newStates = new HashSet<>();
        Map<String, Map<Character, String>> dfaTrans = new HashMap<>();
        Set<String> newFinalStates = new HashSet<>();

        Queue<Set<String>> queue = new LinkedList<>();
        Set<String> startSet = epsilonClosure(Collections.singleton(startState));
        queue.add(startSet);
        String dfaStart = getStateName(startSet);
        newStates.add(dfaStart);
        stateMapping.put(startSet, dfaStart);
        if (!Collections.disjoint(startSet, finalStates))
            newFinalStates.add(dfaStart);

        while (!queue.isEmpty()) {
            Set<String> currSet = queue.poll();
            String currName = stateMapping.get(currSet);
            Map<Character, String> currDfaTrans = new HashMap<>();

            for (char symbol : alphabet) {
                Set<String> nextSet = new HashSet<>();
                for (String s : currSet)
                    if (transitions.containsKey(s) && transitions.get(s).containsKey(symbol))
                        nextSet.addAll(transitions.get(s).get(symbol));
                nextSet = epsilonClosure(nextSet);
                if (!nextSet.isEmpty()) {
                    String nextName = stateMapping.computeIfAbsent(nextSet, k -> getStateName(k));
                    currDfaTrans.put(symbol, nextName);
                    if (newStates.add(nextName)) { // New state found
                        queue.add(nextSet);
                        if (!Collections.disjoint(nextSet, finalStates))
                            newFinalStates.add(nextName);
                    }
                }
            }
            dfaTrans.put(currName, currDfaTrans);
        }
        // Convert DFA transitions to the original format.
        return new FiniteAutomaton(newStates, alphabet,
            convertToDfaTransitions(dfaTrans), dfaStart, newFinalStates);
    }

    // Computes the epsilon closure (if ε-transitions exist).
    private Set<String> epsilonClosure(Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        Stack<String> stack = new Stack<>();
        stack.addAll(states);
        while (!stack.isEmpty()) {
            String state = stack.pop();
            if (transitions.containsKey(state) && transitions.get(state).containsKey('ε')) {
                for (String t : transitions.get(state).get('ε'))
                    if (closure.add(t)) stack.push(t);
            }
        }
        return closure;
    }

    // Combines state names (sorted) into a single string.
    private String getStateName(Set<String> stateSet) {
        return String.join("_", new TreeSet<>(stateSet));
    }

    // Converts DFA transition mapping to the original format.
    private Map<String, Map<Character, Set<String>>> convertToDfaTransitions(
            Map<String, Map<Character, String>> dfaTrans) {
        Map<String, Map<Character, Set<String>>> result = new HashMap<>();
        for (var entry : dfaTrans.entrySet()) {
            Map<Character, Set<String>> inner = new HashMap<>();
            for (var innerEntry : entry.getValue().entrySet())
                inner.put(innerEntry.getKey(), Collections.singleton(innerEntry.getValue()));
            result.put(entry.getKey(), inner);
        }
        return result;
    }

    // Displays the FA components.
    public void display() {
        //prints basically
    }
}


```

#### Main Class
```java
public class Main {
    public static void main(String[] args) {
        // Define states, alphabet and transitions according to Variant...

        String startState = "q0";
        Set<String> finalStates = Set.of("q4");

        FiniteAutomaton ndfa = new FiniteAutomaton(states, alphabet, transitions, startState, finalStates);

        System.out.println("Original NDFA (Variant 3):");
        ndfa.display();

        System.out.println("\nDeterminism Check:");
        if (ndfa.isDeterministic()) {
            System.out.println("The automaton is deterministic.");
        } else {
            System.out.println("The automaton is non-deterministic. Converting to DFA...");
            FiniteAutomaton dfa = ndfa.toDFA();
            System.out.println("\nConverted DFA:");
            dfa.display();
        }
    }
}

```

## Screenshots
![Screenshot 2025-03-02 185951](https://github.com/user-attachments/assets/27823ce8-49f1-4abc-961d-42472ba7ceb6)


## Results
- NDFA Definition: Implemented the finite automaton for Variant 3.
- Determinism Check: The automaton correctly identified as non-deterministic.
- NDFA to DFA Conversion: Successfully converted the NDFA to an equivalent DFA using the subset construction algorithm.
- Chomsky Hierarchy: The project lays the foundation for further extensions (e.g., grammar classification) linking finite automata to regular grammars (Type-3) within the Chomsky hierarchy.
- Graphical Representation: (Optional bonus) Visual representation can be generated using external tools/libraries.

## References

- Hopcroft E. and others. Introduction to Automata Theory, Languages and Computation
- LFPC Guide (ELSE)
- Introduction to formal languages (ELSE)
