import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> states = Set.of("q0", "q1", "q2");
        Set<Character> alphabet = Set.of('a', 'b');

        Map<String, Map<Character, Set<String>>> transitions = new HashMap<>();
        transitions.put("q0", Map.of('a', Set.of("q1"), 'b', Set.of("q0", "q2")));
        transitions.put("q1", Map.of('a', Set.of("q2")));
        transitions.put("q2", Map.of('b', Set.of("q0")));

        String startState = "q0";
        Set<String> finalStates = Set.of("q2");

        FiniteAutomaton nfa = new FiniteAutomaton(states, alphabet, transitions, startState, finalStates);

        System.out.println("Original NFA:");
        nfa.display();

        FiniteAutomaton dfa = nfa.toDFA();

        System.out.println("Converted DFA:");
        dfa.display();
    }
}