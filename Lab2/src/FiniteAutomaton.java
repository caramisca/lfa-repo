import java.util.*;
class FiniteAutomaton {
    private Set<String> states;
    private Set<Character> alphabet;
    private Map<String, Map<Character, Set<String>>> transitions;
    private String startState;
    private Set<String> finalStates;

    public FiniteAutomaton(Set<String> states, Set<Character> alphabet, Map<String, Map<Character, Set<String>>> transitions, String startState, Set<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.startState = startState;
        this.finalStates = finalStates;
    }

    public boolean isDeterministic() {
        for (Map<Character, Set<String>> stateTransitions : transitions.values()) {
            for (Set<String> destStates : stateTransitions.values()) {
                if (destStates.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public DFA toDFA() {
        Set<Set<String>> dfaStates = new HashSet<>();
        Map<Set<String>, Map<Character, Set<String>>> dfaTransitions = new HashMap<>();
        Set<Set<String>> dfaFinalStates = new HashSet<>();

        Set<String> initialState = new HashSet<>(Collections.singleton(startState));
        dfaStates.add(initialState);

        if (!Collections.disjoint(initialState, finalStates)) {
            dfaFinalStates.add(initialState);
        }

        Queue<Set<String>> unmarkedStates = new LinkedList<>();
        unmarkedStates.add(initialState);

        while (!unmarkedStates.isEmpty()) {
            Set<String> currentState = unmarkedStates.poll();
            Map<Character, Set<String>> currentTransitions = new HashMap<>();

            for (char symbol : alphabet) {
                Set<String> newState = new HashSet<>();

                for (String state : currentState) {
                    if (transitions.containsKey(state) && transitions.get(state).containsKey(symbol)) {
                        newState.addAll(transitions.get(state).get(symbol));
                    }
                }

                if (!newState.isEmpty()) {
                    currentTransitions.put(symbol, newState);
                    if (!dfaStates.contains(newState)) {
                        dfaStates.add(newState);
                        unmarkedStates.add(newState);
                        if (!Collections.disjoint(newState, finalStates)) {
                            dfaFinalStates.add(newState);
                        }
                    }
                }
            }
            dfaTransitions.put(currentState, currentTransitions);
        }

        return new DFA(dfaStates, alphabet, dfaTransitions, initialState, dfaFinalStates);
    }

    public String getGrammarType() {
        return "Regular Grammar (Type-3)";
    }
}

class DFA {
    private Set<Set<String>> states;
    private Set<Character> alphabet;
    private Map<Set<String>, Map<Character, Set<String>>> transitions;
    private Set<String> startState;
    private Set<Set<String>> finalStates;

    public DFA(Set<Set<String>> states, Set<Character> alphabet, Map<Set<String>, Map<Character, Set<String>>> transitions, Set<String> startState, Set<Set<String>> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.startState = startState;
        this.finalStates = finalStates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Converted DFA:\n");
        sb.append("  States: ").append(states).append("\n");
        sb.append("  Alphabet: ").append(alphabet).append("\n");
        sb.append("  Start State: ").append(startState).append("\n");
        sb.append("  Final States: ").append(finalStates).append("\n");
        sb.append("  Transitions:\n");

        for (var entry : transitions.entrySet()) {
            for (var trans : entry.getValue().entrySet()) {
                sb.append("    ").append(entry.getKey()).append(" --").append(trans.getKey()).append("--> ").append(trans.getValue()).append("\n");
            }
        }

        return sb.toString();
    }
}
