import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> states = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3", "q4"));
        Set<Character> alphabet = new HashSet<>(Arrays.asList('a', 'b'));

        Map<String, Map<Character, Set<String>>> transitions = new HashMap<>();

        transitions.put("q0", Map.of('a', Set.of("q1")));
        transitions.put("q1", Map.of('b', Set.of("q1"), 'a', Set.of("q2")));
        transitions.put("q2", Map.of('b', Set.of("q2", "q3")));
        transitions.put("q3", Map.of('b', Set.of("q4"), 'a', Set.of("q1")));

        String startState = "q0";
        Set<String> finalStates = Set.of("q4");

        FiniteAutomaton fa = new FiniteAutomaton(states, alphabet, transitions, startState, finalStates);
        System.out.println("Automaton Type: " + (fa.isDeterministic() ? "DFA" : "NDFA"));

        DFA dfa = fa.toDFA();
        System.out.println("\n" + dfa);

        System.out.println("\nGrammar Classification: " + fa.getGrammarType());
    }
}
