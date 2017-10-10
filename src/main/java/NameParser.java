import java.util.*;
import java.util.stream.Collectors;

/**
 * Class for checking if the user's input of
 * full name & last name is correct by comparison
 * of them.
 */
public class NameParser {

    /**
     * Method gets the full name, trims the String, removes extra spaces,
     * splits full name into parts and gets the first word from full name.
     * If there is a suffix from list, it ignores it and takes the next word from name.
     *
     * @param fullName the full name which was entered
     * @return the first name from the full name
     */
    public String getFirstName(String fullName) {

        String firstName;

        if (null == fullName)
            fullName = "";

        String nameInParts[] = fullName.trim().replaceAll("\\s+", " ").split(" ");

        boolean isMatch = Arrays.stream(KnownSuffixes.KNOWN_SUFFIXES) //checks if the first
                .flatMap(Arrays::stream)                              //word in name is a suffix
                .anyMatch(s -> s.equalsIgnoreCase(nameInParts[0]));

        firstName = (isMatch ? nameInParts[1] : nameInParts[0]);
        return firstName;
    }

    /**
     * Checks compliance with the rules of names input
     *
     * @param lastName the last name that was entered
     * @param fullName the full name that was entered
     * @return true if the name was entered correctly
     */
    public boolean matchName(String lastName, String fullName) {

        List<String> last = parseInput(lastName);
        List<String> full = parseInput(fullName);

        Stack<String> stackLast = new Stack<>();
        Stack<String> stackFull = new Stack<>();

        stackLast.addAll(last);
        stackFull.addAll(full);

        if (stackFull.isEmpty()) {
            return false;
        }

        while (!stackLast.isEmpty()) {

            String tokenLast = stackLast.peek();
            String tokenFull = stackFull.peek();

            boolean isLastSuf = isValidSuffix(tokenLast);
            boolean isFullSuf = isValidSuffix(tokenFull) || isValidSuffix(tokenFull.replaceAll("[.,!?\\-]", ""));

            if (isLastSuf && isFullSuf) {
                if (checkSuffixesEqual(tokenFull, tokenLast)) {
                    stackFull.pop();
                    stackLast.pop();
                } else {
                    return false;
                }
                continue;
            }

            if (isFullSuf) {
                stackFull.pop();
            }

            if (isLastSuf) {
                stackLast.pop();
            }

            if (!isLastSuf && !isFullSuf) {
                return compareBySymbol(new ArrayList<>(stackLast), new ArrayList<>(stackFull));
            }
        }
        return true;
    }


    private List<String> removeReduction(List<String> list) {

        for (int j = 0; j < list.size(); j++) {
            String currentValue = list.get(j);

            if ((currentValue.length() == 1 && j < list.size() - 1) ||
                    (currentValue.length() == 2 && currentValue.endsWith("."))) {
                list.remove(j);
            }
        }
        return list;
    }

    private boolean compareBySymbol(List<String> lastName, List<String> fullName) {

        List<String> last = removeReduction(lastName);
        List<String> full = removeReduction(fullName);

        Stack<String> lastNameStack = listToCharStack(last);
        Stack<String> fullNameStack = listToCharStack(full);

        while (lastNameStack.size() != 0 && fullNameStack.size() != 0) {

            String lastNameChar = lastNameStack.peek();
            String fullNameChar = fullNameStack.peek();

            if (fullNameChar.equalsIgnoreCase(lastNameChar)) {
                lastNameStack.pop();
                fullNameStack.pop();
            } else if (lastNameChar.equals(" ") && fullNameChar.equals("-")) {
                return false;
            } else if (lastNameChar.equals("'") || lastNameChar.equals("-")) {
                lastNameStack.pop();
            } else if (fullNameChar.equals("'") || fullNameChar.equals("-")) {
                fullNameStack.pop();
            } else if ((lastNameChar.equals(" ") && !fullNameChar.equals(" ")) ||
                    (!lastNameChar.equals(" ") && fullNameChar.equals(" "))) {
                if (lastNameChar.equals(" ")) {
                    lastNameStack.pop();
                }
                if (fullNameChar.equals(" ")) {
                    fullNameStack.pop();
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private Stack<String> listToCharStack(List<String> list) {
        Stack<String> stack = new Stack<>();

        List<String> newArrayList = new ArrayList<>();

        for (Character c : String.join(" ", list).toCharArray()) {
            newArrayList.add(c.toString());
        }

        stack.addAll(newArrayList);

        return stack;
    }

    private List<String> parseInput(String input) {

        List<String> parsed = Arrays.stream(input.split(" "))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<String> newParsed = new ArrayList<>();

        for (String str1 : parsed) {
            String[] arr = str1.split("\\d+");
            if (arr.length > 1) {
                if (arr[0].length() != 0) {
                    newParsed.add(arr[0]);
                }
                newParsed.add(str1.replaceAll(arr[0], ""));
            } else {
                newParsed.add(str1);
            }
        }
        return newParsed;
    }

    private boolean isValidSuffix(String suffix) {

        return Arrays.stream(KnownSuffixes.KNOWN_SUFFIXES)
                .flatMap(Arrays::stream)
                .anyMatch(suffix::equalsIgnoreCase);
    }

    private boolean checkSuffixesEqual(String suffix0, String suffix1) {

        Optional<Set<String>> optional = Arrays.stream(KnownSuffixes.KNOWN_SUFFIXES)
                .map(suffixes -> Arrays.stream(suffixes)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet()))
                .filter(set -> set.contains(suffix0.toLowerCase()))
                .findFirst();

        boolean equals = optional.isPresent() && optional.get().contains(suffix1.toLowerCase());

        if (!equals) {
            return suffix0.replaceAll("[.,!?\\-]", "").equalsIgnoreCase(suffix1.replaceAll("[.,!?\\-]", ""));
        }

        return equals;
    }

    public static void main(String[] args) {

    }
}