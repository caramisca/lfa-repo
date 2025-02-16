import java.util.*;


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
