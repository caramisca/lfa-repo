package cnf;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private Set<String> nonTerminals;
    private Set<String> terminals;
    private Map<String, List<List<String>>> productions;
    private String startSymbol;

    public Grammar(Set<String> nonTerminals, Set<String> terminals,
                   Map<String, List<List<String>>> productions, String startSymbol) {
        this.nonTerminals = new HashSet<>(nonTerminals);
        this.terminals = new HashSet<>(terminals);
        this.productions = new HashMap<>();
        for (Map.Entry<String, List<List<String>>> e : productions.entrySet()) {
            this.productions.put(e.getKey(), new ArrayList<>());
            for (List<String> rhs : e.getValue()) {
                this.productions.get(e.getKey()).add(new ArrayList<>(rhs));
            }
        }
        this.startSymbol = startSymbol;
    }

    private void eliminateEpsilonProductions() {
        Set<String> nullable = new HashSet<>();
        boolean changed;
        do {
            changed = false;
            for (String A : new HashSet<>(nonTerminals)) {
                for (List<String> rhs : productions.getOrDefault(A, List.of())) {
                    if (rhs.stream().allMatch(s -> s.equals("ε") || nullable.contains(s))) {
                        if (nullable.add(A)) changed = true;
                        break;
                    }
                }
            }
        } while (changed);

        Map<String, List<List<String>>> newP = new HashMap<>();
        for (String A : nonTerminals) {
            newP.put(A, new ArrayList<>());
            for (List<String> rhs : productions.getOrDefault(A, List.of())) {
                List<List<String>> variants = new ArrayList<>();
                variants.add(rhs);
                for (int i = 0; i < rhs.size(); i++) {
                    String sym = rhs.get(i);
                    if (nullable.contains(sym)) {
                        List<List<String>> next = new ArrayList<>();
                        for (List<String> v : variants) {
                            List<String> copy = new ArrayList<>(v);
                            copy.remove(i);
                            next.add(copy);
                        }
                        variants.addAll(next);
                    }
                }
                for (List<String> v : variants) {
                    if (v.isEmpty()) newP.get(A).add(List.of("ε"));
                    else newP.get(A).add(v);
                }
            }
        }
        productions = newP;
    }

    private void eliminateUnitProductions() {
        Map<String, Set<String>> unitGraph = new HashMap<>();
        for (String A : nonTerminals) {
            unitGraph.put(A, new HashSet<>());
            for (List<String> rhs : productions.getOrDefault(A, List.of())) {
                if (rhs.size() == 1 && nonTerminals.contains(rhs.get(0))) {
                    unitGraph.get(A).add(rhs.get(0));
                }
            }
        }
        for (String A : nonTerminals) {
            Deque<String> stack = new ArrayDeque<>(unitGraph.get(A));
            while (!stack.isEmpty()) {
                String B = stack.pop();
                for (String C : unitGraph.getOrDefault(B, Set.of())) {
                    if (unitGraph.get(A).add(C)) stack.push(C);
                }
            }
        }
        Map<String, List<List<String>>> newP = new HashMap<>();
        for (String A : nonTerminals) {
            Set<List<String>> set = new HashSet<>();
            for (List<String> rhs : productions.getOrDefault(A, List.of())) {
                if (!(rhs.size()==1 && nonTerminals.contains(rhs.get(0)))) set.add(rhs);
            }
            for (String B : unitGraph.get(A)) {
                for (List<String> rhs : productions.getOrDefault(B, List.of())) {
                    if (!(rhs.size()==1 && nonTerminals.contains(rhs.get(0)))) set.add(rhs);
                }
            }
            newP.put(A, new ArrayList<>(set));
        }
        productions = newP;
    }

    private void eliminateInaccessibleSymbols() {
        Set<String> reachable = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        reachable.add(startSymbol);
        stack.push(startSymbol);
        while (!stack.isEmpty()) {
            String A = stack.pop();
            for (List<String> rhs : productions.getOrDefault(A, List.of())) {
                for (String s : rhs) {
                    if (nonTerminals.contains(s) && reachable.add(s)) stack.push(s);
                }
            }
        }
        nonTerminals.retainAll(reachable);
        productions.keySet().retainAll(reachable);
    }

    private void eliminateNonProductiveSymbols() {
        Set<String> productive = new HashSet<>();
        boolean changed;
        do {
            changed = false;
            for (String A : nonTerminals) {
                for (List<String> rhs : productions.getOrDefault(A, List.of())) {
                    if (rhs.stream().allMatch(s -> terminals.contains(s) || productive.contains(s))) {
                        if (productive.add(A)) changed = true;
                    }
                }
            }
        } while (changed);
        nonTerminals.retainAll(productive);
        productions.keySet().retainAll(productive);
    }

    private void toChomskyNormalForm() {
        Map<String, String> termMap = new HashMap<>();
        for (String t : terminals) {
            String nt = "T_" + t;
            termMap.put(t, nt);
            nonTerminals.add(nt);
            productions.put(nt, List.of(List.of(t)));
        }

        Map<String, List<List<String>>> newP = new HashMap<>();
        int count = 1;
        for (String A : productions.keySet()) {
            newP.computeIfAbsent(A, k -> new ArrayList<>());
            for (List<String> rhs : productions.get(A)) {
                List<String> modified = rhs.stream()
                        .map(s -> terminals.contains(s) && rhs.size()>1 ? termMap.get(s) : s)
                        .collect(Collectors.toList());

                if (modified.size() <= 2) {
                    newP.get(A).add(modified);
                } else {
                    List<String> curr = new ArrayList<>(modified);
                    String left = A;
                    while (curr.size() > 2) {
                        String X = "X" + (count++);
                        nonTerminals.add(X);
                        newP.computeIfAbsent(left, k -> new ArrayList<>())
                                .add(List.of(curr.get(0), X));
                        curr = curr.subList(1, curr.size());
                        left = X;
                    }
                    newP.computeIfAbsent(left, k -> new ArrayList<>()).add(new ArrayList<>(curr));
                }
            }
        }
        productions = newP;
    }

    public void normalizeToCNF() {
        eliminateEpsilonProductions();
        eliminateUnitProductions();
        eliminateInaccessibleSymbols();
        eliminateNonProductiveSymbols();
        toChomskyNormalForm();
    }

    public void printGrammar() {
        System.out.println("Start: " + startSymbol);
        System.out.println("N: " + nonTerminals);
        System.out.println("T: " + terminals);
        System.out.println("P:");
        for (String A : productions.keySet()) {
            for (List<String> rhs : productions.get(A)) {
                System.out.println("  " + A + " -> " + String.join(" ", rhs));
            }
        }
    }

    public static void main(String[] args) {
        Set<String> Vn = Set.of("S","A","B","C","E");
        Set<String> Vt = Set.of("a","d");
        String S = "S";
        Map<String,List<List<String>>> P = new HashMap<>();
        //Variant 3
        P.put("S", List.of(List.of("d","B"), List.of("A")));
        P.put("A", List.of(List.of("d"), List.of("d","S"), List.of("a","A","d","A","B")));
        P.put("B", List.of(List.of("a","C"), List.of("a","S"), List.of("A","C")));
        P.put("C", List.of(List.of("ε")));
        P.put("E", List.of(List.of("A","S")));
        Grammar g = new Grammar(Vn, Vt, P, S);
        System.out.println("Original:"); g.printGrammar();
        g.normalizeToCNF();
        System.out.println("\nCNF:"); g.printGrammar();
    }
}
