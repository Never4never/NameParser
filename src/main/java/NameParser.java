import java.util.*;
import java.util.stream.Collectors;


public class NameParser {

    private String firstName;
    private static String lastName = "DeCullies";
    private static String fullName = "Decullies";

    /**
     * Constuctor
     */
    public NameParser() {
        this.lastName = lastName;
        this.fullName = fullName;
    }

    /**
     * Method gets the full name, trims the String, removes extra spaces,
     * splits full name into parts and gets the first word from full name.
     * <p>
     * If there is a suffix from list, it ignores it and takes the next word from name.
     *
     * @param fullName the full name which was entered
     * @return the first name from the full name
     */
    public String getFirstName(String fullName) {

        String nameInParts[] = fullName.trim().replaceAll("\\s+", " ").split(" ");

        boolean isMatch = Arrays.stream(KnownSuffixes.KNOWN_SUFFIXES) //checks if the first
                .flatMap(Arrays::stream)                              //word in name is a suffix
                .anyMatch(s -> s.equalsIgnoreCase(nameInParts[0]));

        firstName = (isMatch ? nameInParts[1] : nameInParts[0]);
        System.out.println("First Name is: " + firstName);
        return firstName;
    }

    public boolean matchName(String lastName, String fullName) {
        List last = parseInput(lastName);
        List full = parseInput(fullName);
        Stack<String> stackLast = new Stack<>();
        Stack<String> stackFull = new Stack<>();
        stackLast.addAll(last);
        stackFull.addAll(full);
        while (!stackLast.isEmpty()) {
            String tokenLast = stackLast.peek();
            String tokenFull = stackFull.peek();
            boolean isLastSuf = isValidSuffixe(tokenLast);
            boolean isFullSuf = isValidSuffixe(tokenFull);
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
                return tokenFull.equalsIgnoreCase(tokenLast);
            }
        }
        return true;
    }

    public List parseInput(String input) {
        return Arrays.stream(input.split(" ")).map(String::trim).filter(s -> s.length() > 0).collect(Collectors.toList());
    }

    public boolean isValidSuffixe(String suffix) {
        return Arrays.stream(KnownSuffixes.KNOWN_SUFFIXES).flatMap(Arrays::stream).anyMatch(suffix::equalsIgnoreCase);
    }

    public boolean checkSuffixesEqual(String suffix0, String suffix1) {

        Optional<Set<String>> optional = Arrays.stream(KnownSuffixes.KNOWN_SUFFIXES)
                .map(suffixes -> Arrays.stream(suffixes)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet()))
                .filter(set -> set.contains(suffix0.toLowerCase()))
                .findFirst();

        return optional.isPresent() && optional.get().contains(suffix1.toLowerCase());
    }


    public static void main(String[] args) {
        NameParser parser = new NameParser();

    }
}