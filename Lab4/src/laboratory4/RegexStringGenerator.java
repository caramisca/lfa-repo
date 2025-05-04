package laboratory4;

import java.util.Random;

public class RegexStringGenerator {
    private static final Random random = new Random();

    public static void main(String[] args) {
        // Given Regular Expressions
        String regex1 = "O(P|Q|R)+2(3|4)";
        String regex2 = "A*B(C|D|E)F(G|H|I)^2";
        String regex3 = "J+K(L|M|N)*?(P|Q)^3";

        System.out.println("Generated strings:");
        System.out.println(generateStringFromRegex(regex1));
        System.out.println(generateStringFromRegex(regex2));
        System.out.println(generateStringFromRegex(regex3));
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
                        String selected = choices[random.nextInt(choices.length)];

                        if (endIndex + 1 < regex.length()) {
                            char next = regex.charAt(endIndex + 1);

                            if (next == '+') {
                                int repeat = 1 + random.nextInt(5);
                                result.append(selected.repeat(repeat));
                                index = endIndex + 1; // skip '+'
                            } else if (next == '*') {
                                int repeat = random.nextInt(6);
                                result.append(selected.repeat(repeat));
                                index = endIndex + 1; // skip '*'
                            } else if (next == '^') {
                                if (endIndex + 2 < regex.length() && Character.isDigit(regex.charAt(endIndex + 2))) {
                                    int repeat = Character.getNumericValue(regex.charAt(endIndex + 2));
                                    result.append(selected.repeat(repeat));
                                    index = endIndex + 2; // skip '^' and digit
                                } else {
                                    result.append(selected);
                                    index = endIndex;
                                }
                            } else {
                                result.append(selected);
                                index = endIndex;
                            }
                        } else {
                            result.append(selected);
                            index = endIndex;
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
                    break;
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
