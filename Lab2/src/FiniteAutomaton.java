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

    // Check if FA is deterministic
    public boolean isDeterministic() {
        for (Map<Character, Set<String>> stateTransitions : transitions.values()) {
            for (Set<String> targets : stateTransitions.values()) {
                if (targets.size() > 1) {
                    return false; // Multiple target states for a symbol -> NDFA
                }
            }
        }
        return true;
    }

    // Convert NDFA to DFA
    public FiniteAutomaton toDFA() {
        Map<Set<String>, String> dfaStatesMapping = new HashMap<>();
        Set<String> newStates = new HashSet<>();
        Map<String, Map<Character, String>> dfaTransitions = new HashMap<>();
        Set<String> newFinalStates = new HashSet<>();

        Queue<Set<String>> queue = new LinkedList<>();
        Set<String> startSet = epsilonClosure(Collections.singleton(startState));
        queue.add(startSet);

        String dfaStartState = getStateName(startSet);
        newStates.add(dfaStartState);
        dfaStatesMapping.put(startSet, dfaStartState);

        if (!Collections.disjoint(startSet, finalStates)) {
            newFinalStates.add(dfaStartState);
        }

        while (!queue.isEmpty()) {
            Set<String> currentSet = queue.poll();
            String currentStateName = dfaStatesMapping.get(currentSet);

            Map<Character, String> currentDfaTransitions = new HashMap<>();

            for (char symbol : alphabet) {
                Set<String> nextStateSet = new HashSet<>();
                for (String state : currentSet) {
                    if (transitions.containsKey(state) && transitions.get(state).containsKey(symbol)) {
                        nextStateSet.addAll(transitions.get(state).get(symbol));
                    }
                }

                nextStateSet = epsilonClosure(nextStateSet);

                if (!nextStateSet.isEmpty()) {
                    String nextStateName = dfaStatesMapping.computeIfAbsent(nextStateSet, k -> getStateName(k));
                    currentDfaTransitions.put(symbol, nextStateName);

                    if (!newStates.contains(nextStateName)) {
                        newStates.add(nextStateName);
                        queue.add(nextStateSet);

                        if (!Collections.disjoint(nextStateSet, finalStates)) {
                            newFinalStates.add(nextStateName);
                        }
                    }
                }
            }
            dfaTransitions.put(currentStateName, currentDfaTransitions);
        }

        return new FiniteAutomaton(newStates, alphabet, convertToDfaTransitions(dfaTransitions), dfaStartState, newFinalStates);
    }

    // Helper to get epsilon closure of a state set
    private Set<String> epsilonClosure(Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        Stack<String> stack = new Stack<>();
        stack.addAll(states);

        while (!stack.isEmpty()) {
            String state = stack.pop();
            if (transitions.containsKey(state) && transitions.get(state).containsKey('ε')) {
                for (String epsilonTarget : transitions.get(state).get('ε')) {
                    if (closure.add(epsilonTarget)) {
                        stack.push(epsilonTarget);
                    }
                }
            }
        }
        return closure;
    }

    // Helper to get a combined state name
    private String getStateName(Set<String> stateSet) {
        return String.join("_", new TreeSet<>(stateSet));
    }

    // Convert DFA transitions map to required format
    private Map<String, Map<Character, Set<String>>> convertToDfaTransitions(Map<String, Map<Character, String>> dfaTransitions) {
        Map<String, Map<Character, Set<String>>> result = new HashMap<>();
        for (Map.Entry<String, Map<Character, String>> entry : dfaTransitions.entrySet()) {
            result.put(entry.getKey(), new HashMap<>());
            for (Map.Entry<Character, String> innerEntry : entry.getValue().entrySet()) {
                result.get(entry.getKey()).put(innerEntry.getKey(), Collections.singleton(innerEntry.getValue()));
            }
        }
        return result;
    }

    public void display() {
        System.out.println("States: " + states);
        System.out.println("Alphabet: " + alphabet);
        System.out.println("Start State: " + startState);
        System.out.println("Final States: " + finalStates);
        System.out.println("Transitions: " + transitions);
    }
}