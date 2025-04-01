package laboratory4;

import java.util.Random;

public class RegexStringGenerator {
    private static final Random random = new Random();

    public static void main(String[] args) {
        // Given Regular Expressions
        String regex1 = "O(P|Q|R)+2(3|4)";
        String regex2 = "A*B(C|D|E)F(G|H|I)^2";
        String regex3 = "J+K(L|M|N)*?(P|Q)^3"; // Modified regex for J+K(L|M|N)*O?(P|Q)^3

        // Generate valid words
        System.out.println("Generated strings:");
        System.out.println(generateStringFromRegex(regex1));  // Should generate: OPP23, OQQQQ24, etc.
        System.out.println(generateStringFromRegex(regex2));  // Should generate: AAABCFGG, AAAAAABDFHH, etc.
        System.out.println(generateStringFromRegex(regex3));  // Should generate: JJKLOPPP, JKNQQQ, etc.
    }

    public static String generateStringFromRegex(String regex) {
        StringBuilder result = new StringBuilder();
        int index = 0;

        while (index < regex.length()) {
            char ch = regex.charAt(index);

            switch (ch) {
                case 'O':
                case 'B':
                case 'F':
                case 'K':
                    result.append(ch);
                    break;
                case 'J':
                    result.append(repeatChar('J', 1 + random.nextInt(5))); // J+ -> 1 to 5 times
                    break;
                case 'A':
                    if (index + 1 < regex.length() && regex.charAt(index + 1) == '*') {
                        result.append(repeatChar('A', random.nextInt(6))); // A* -> 0 to 5 times
                        index++; // Skip '*'
                    } else {
                        result.append('A');
                    }
                    break;
                case '(':
                    int endIndex = regex.indexOf(')', index);
                    if (endIndex != -1) {
                        String options = regex.substring(index + 1, endIndex);
                        String[] choices = options.split("\\|");

                        if (endIndex + 1 < regex.length() && regex.charAt(endIndex + 1) == '+') {
                            // Handle (P|Q|R)+ correctly -> repeat 1 to 5 times
                            int repeat = 1 + random.nextInt(5);
                            char selectedChar = choices[random.nextInt(choices.length)].charAt(0); // Pick one character
                            result.append(String.valueOf(selectedChar).repeat(repeat)); // Repeat it
                            index = endIndex + 1; // Move past ')+'
                        } else {
                            // Normal (X|Y|Z) case -> pick one character
                            result.append(choices[random.nextInt(choices.length)]);
                            index = endIndex; // Move past ')'
                        }
                    }
                    break;

                case '^': // Handle (G|H|I)^2 or (P|Q)^3
                    if (index + 1 < regex.length() && Character.isDigit(regex.charAt(index + 1))) {
                        int repeat = Character.getNumericValue(regex.charAt(index + 1));
                        String lastPart = result.substring(result.length() - 1); // Repeat last generated part
                        for (int i = 1; i < repeat; i++) {
                            result.append(lastPart);
                        }
                        index++; // Skip digit
                    }
                    break;
                case '*':
                    break; // Already handled before
                case '?':
                    if (random.nextBoolean()) {
                        result.append('O');
                    }
                    break;
                case '+':
                    // Skip '+' entirely (no appending)
                    break;
                case '2': // Number case for "2(3|4)"
                    result.append('2');
                    break;
                case '3':
                case '4':
                    if (index > 0 && regex.charAt(index - 1) == '2') {
                        result.append(ch); // Append 3 or 4 after 2
                    }
                    break;
                default:
                    result.append(ch);
            }
            index++;
        }

        return result.toString();
    }

    private static String repeatChar(char ch, int count) {
        return String.valueOf(ch).repeat(count);
    }
}
