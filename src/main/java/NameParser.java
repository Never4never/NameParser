

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


    /**
     * Method gets the full name, calls method nameWithoutSuffixes
     * <p>
     * If there is a suffix from list, it ignores it and takes the previous word from name.
     *
     * @param fullName the full name which was entered
     * @return he last name from the full name
     */
    public String getLastName(String fullName) {

        List<String> names = nameWithoutSuffixes(fullName);

        System.out.println("lastname: " + names.get(names.size() - 1));
        return names.get(names.size() - 1);
    }


    /**
     * Method gets the full name, trims the string,
     * removes extra spaces and splits the full name into parts.
     * <p>
     * If there is a suffix from list, returns true.
     *
     * @param fullName the full name which was entered
     * @return the result of comparison -- if full name cotains any suffixes
     */
    public boolean ifSuffixExists(String fullName) {

        Set<String> nameInParts = new HashSet<>(Arrays.asList(fullName.trim().toLowerCase().replaceAll("\\s+", " ").split(" ")));

        boolean isMatch = Arrays.stream(KnownSuffixes.KNOWN_SUFFIXES)
                .flatMap(Arrays::stream)
                .anyMatch(s -> nameInParts.contains(s)); //compares the whole nameInParts with suffixes list

        System.out.println("Suffix existing: " + isMatch);

        return isMatch;
    }


    /**
     * Method gets the full name, split into parts by space,
     * trims the String
     * takes ones without spaces
     * and compares with the list of suffixes.
     * Then collects the result into the list.
     *
     * @param fullName the full name which was entered
     * @return list of parts of name without any suffixes
     */
    public List<String> nameWithoutSuffixes(String fullName) {

        return Arrays.stream(fullName.split(" "))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .filter(s -> Arrays.stream(KnownSuffixes.KNOWN_SUFFIXES)
                        .flatMap(Arrays::stream)
                        .noneMatch(s::equalsIgnoreCase))
                .collect(Collectors.toList());

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



    public static String ifMergedSuffixes (String name){

        String numbers[] = {"1","2","3","4","5","6","7","8","9"};

        for (int i = 0; i<= numbers.length-1; i++) {
            if (name.contains(numbers[i])) {

                name = name.replace(" "+numbers[i], numbers[i]);
            }
        }
        return name;
    }



    public static void main(String[] args) {
        NameParser parser = new NameParser();
        parser.ifSuffixExists(fullName);
        parser.getLastName(fullName);
        System.out.println(String.join(" ", parser.nameWithoutSuffixes(fullName)));

        parser.matchName(lastName, fullName);
        System.out.println(ifMergedSuffixes(fullName).equalsIgnoreCase(ifMergedSuffixes(lastName)));

}