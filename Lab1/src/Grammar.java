import java.util.*;

class Grammar {
    private Set<Character> VN; // Variabile neterminale
    private Set<Character> VT; // Simboluri terminale
    private Map<Character, List<String>> P; // Producții
    private char startSymbol;

    public Grammar(Set<Character> VN, Set<Character> VT, Map<Character, List<String>> P, char startSymbol) {
        this.VN = VN;
        this.VT = VT;
        this.P = P;
        this.startSymbol = startSymbol;
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
                current = selectedProduction.charAt(1); // Continuă cu următorul simbol
            else
                break; // Termină generarea dacă e simbol terminal
        }
        return result.toString();
    }

    public FiniteAutomaton toFiniteAutomaton() {
        Set<String> states = new HashSet<>();
        Set<Character> sigma = new HashSet<>(VT);
        Map<String, Map<Character, String>> transitions = new HashMap<>();
        String startState = String.valueOf(startSymbol);
        Set<String> finalStates = new HashSet<>();

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
