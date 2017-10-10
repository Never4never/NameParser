import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class NameParserTest {

    @InjectMocks
    NameParser parser = new NameParser();

    @Test
    public void testMatchMyName() {
        //fail("Not yet implemented");
    }

    @Test
    public void testName() {
        assertEquals(false, parser.matchName(null, null));

        assertEquals(true, parser.matchName("Smith", "John Smith"));
        assertEquals(true, parser.matchName("Smith", "John E Smith"));
        assertEquals(true, parser.matchName("Smith", "John Edward Smith"));
        assertEquals(true, parser.matchName("Smith Jr", "John Edward Smith"));
        assertEquals(true, parser.matchName("Smith Jr ", "John Edward Smith"));
        assertEquals(true, parser.matchName("Smith", "John Edward Smith Jr."));
        assertEquals(true, parser.matchName("Smith", "John Edward Smith III"));
        assertEquals(false, parser.matchName("Smith", "John Smith Edward"));

        assertEquals(true, parser.matchName("Smith Sr", "John Edward Smith Sr."));
        assertEquals(false, parser.matchName("Jones Jr.", "John Edward Smith jr."));
        assertEquals(false, parser.matchName("Jones Jr.   ", "John Edward Smith jr "));
        assertEquals(false, parser.matchName("Jones Jr.", "John Edward Smith Sr."));
        assertEquals(false, parser.matchName("John Edward Smith Jr.", "John Edward Smith Sr."));
        assertEquals(false, parser.matchName("John-Smith Jr.", "John Edward Smith Jr"));

    }


    @Test
    public void unsupportedSuffixes() {
        assertEquals(false, parser.matchName("Smith", "John Edward Smith jnr"));
        assertEquals(false, parser.matchName("Jones Jr.   ", "John Edward Smith JNR "));
        assertEquals(false, parser.matchName("Smith Jr", "John Edward Smith Jnr"));
        assertEquals(false, parser.matchName("John Edward Smith III Jr.", "John Edward Smith jnR"));
        assertEquals(false, parser.matchName("John Edward Smith Jr.", "John Edward Smith jNR"));
        assertEquals(false, parser.matchName("Smith SR", "John Edward Smith Snr"));
        assertEquals(false, parser.matchName("Rose SR", "John Edward Smith SNR"));
        assertEquals(false, parser.matchName("Smith III Jr.", "John Edward Smith JNR"));
        assertEquals(false, parser.matchName("Smith junior", "John Edward Smith Jnr"));
        assertEquals(false, parser.matchName("Smith jnr", "John Edward Smith Jr."));
        assertEquals(false, parser.matchName("Smith    PhD.", "Smith PhD"));
        assertEquals(false, parser.matchName("Li", "Adam Li I"));
        assertEquals(false, parser.matchName("Li", "Li Li I"));

    }

    @Test
    public void suffixIButPasses() {
        assertEquals(true, parser.matchName("Lii I", "Li Lii I"));

    }

    @Test
    public void testDubiousTestCases() {
        assertEquals(true, parser.matchName("Smith sr", "John Edward Smith senior"));

    }

    @Test
    public void testSpacesBetweenName() {
        assertEquals(true, parser.matchName("Smith    Jr.", "Smith Jr"));
        assertEquals(true, parser.matchName("         Smith    Jr.     ", "  Smith Jr"));
        assertEquals(false, parser.matchName("Smith 9th", "Adam Smith"));
    }

    @Test
    public void testDoctorates() {
        assertEquals(true, parser.matchName("Smith PhD", "Smith phd"));
        assertEquals(false, parser.matchName("SmithPhD", "Smith phd"));

    }

    @Test
    public void testDDSDoctors() {
        assertEquals(false, parser.matchName("Smith    dds", "Smith PhD"));
        assertEquals(true, parser.matchName("Smith dds", "Smith DDS"));
        assertEquals(true, parser.matchName("Smith DDS", "Smith D.D.S."));
        assertEquals(true, parser.matchName("Smith DDS", "Smith"));

    }

    @Test
    public void testTrailingWhiteSpaceInDBField() {
        assertEquals(false, parser.matchName("Smith    dds", "Smith PhD  "));
        assertEquals(true, parser.matchName("Smith dds", "Smith DDS"));
        assertEquals(true, parser.matchName("Smith DDS", "Smith D.D.S.			"));
        assertEquals(true, parser.matchName("Smith DDS", "Smith "));
        assertEquals(true, parser.matchName("Smith   DDS", "Smith "));

    }

    @Test
    public void testTrailingWhiteSpaceInCustomerInput() {
        assertEquals(false, parser.matchName("Smith    dds	", "Smith PhD"));
        assertEquals(true, parser.matchName("Smith   ", "Smith DDS"));
        assertEquals(true, parser.matchName("Smith DDS    ", "Smith D.D.S."));
        assertEquals(true, parser.matchName("Smith DDS			", "Smith"));

    }

    @Test
    public void testLeadingWhiteSpaceInDataBase() {
        assertEquals(false, parser.matchName("Smith    dds", " Smith PhD"));
        assertEquals(true, parser.matchName("Smith", "  Smith DDS"));
        assertEquals(true, parser.matchName("Smith DDS", "			Smith D.D.S."));
        assertEquals(true, parser.matchName("Smith DDS", "   	Smith"));

    }

    @Test
    public void testLeadingWhiteSpaceInCustomerInput() {
        assertEquals(false, parser.matchName("	Smith    dds", "Smith PhD"));
        assertEquals(true, parser.matchName("  Smith", "  Smith DDS"));
        assertEquals(true, parser.matchName(" Smith DDS", "Smith D.D.S."));
        assertEquals(true, parser.matchName("    		Smith DDS", "Smith"));

    }

    @Test
    public void testSomeNumbers() {
        assertEquals(false, parser.matchName("Smith 4th", "Smith 3rd"));
        assertEquals(true, parser.matchName("Smith 4th", "Smith 4th	"));

    }

    @Test
    public void testPuncutations() {
        assertEquals(true, parser.matchName("Smith DDS", "Smith D.D.S."));

    }

    @Test
    public void testEsq() {
        assertEquals(true, parser.matchName("Smith Esq", "Smith Esq."));
        assertEquals(true, parser.matchName("Smith Esq", "Smith Esq.  "));
        assertEquals(false, parser.matchName("Smith Esq", "Smith cpa"));


    }

    @Test
    public void testNameContainsTheSuffixEdgeCases() {
        assertEquals(false, parser.matchName("Jonesr", "Adam Smith"));
        assertEquals(false, parser.matchName("Smithjr", "Adam Smith Jr"));
        assertEquals(true, parser.matchName("Smithjr", "Adam SmithJr"));
        assertEquals(false, parser.matchName("Smithjr", "Adam Smith Sr"));
        assertEquals(false, parser.matchName("Smithjr", "Adam SmithSr"));
        assertEquals(true, parser.matchName("Jones 4th", "Adam Jones IV"));
        assertEquals(true, parser.matchName("Jones4th", "Adam Jones 4th"));
    }

    @Test
    public void testReallySmallNames() {
        assertEquals(true, parser.matchName("Li", "Adam Li"));
        assertEquals(true, parser.matchName("Li", "Adam Li II"));
        assertEquals(true, parser.matchName("Lii", "Li Lii II"));
        assertEquals(true, parser.matchName("Lii", "Li Lii III"));
        assertEquals(true, parser.matchName("Lii", "Li Lii Lii"));
        assertEquals(false, parser.matchName("Lii II", "Li Li II"));

    }

    @Test
    public void testSomeWeirdOutOfOrdinarySuffixes() {
        assertEquals(false, parser.matchName("Lee", "Lee Coolguy"));
        assertEquals(false, parser.matchName("Lee IceT", "Lee Coolguy"));
        assertEquals(true, parser.matchName("Lee nice", "Lee nice"));
    }

    @Test
    public void test_Suffix_5() {
        assertEquals(true, parser.matchName("Lee v", "Lee V."));
        assertEquals(true, parser.matchName("Lee v.", "Lee FIFTH"));
        assertEquals(true, parser.matchName("Lee 5th", "Lee V"));
    }

    @Test
    public void testUSA_F_R_Suffixes() {
        assertEquals(true, parser.matchName("Lee USAF", "Lee US.AF"));
        assertEquals(true, parser.matchName("Lee USAF", "Bret M Lee U.S.A.F."));
        assertEquals(false, parser.matchName("Lee USAF", "Bret M Lee U.S.A."));
        assertEquals(false, parser.matchName("Lee USA", "Bret M Lee USAF"));
        assertEquals(true, parser.matchName("Lee USAF", "Lee .USAF"));
        assertEquals(true, parser.matchName("Lee USAF", "Lee U.S.A.F.,"));

        assertEquals(false, parser.matchName("Lee USAF", "Lee U.S.A.F.R."));
        assertEquals(false, parser.matchName("Lee USAF", "Lee U.S.A.R."));
        assertEquals(true, parser.matchName("Lee USAF", "Lee USAF"));


    }


    @Test
    public void testUSMC_Suffixes() {
        assertEquals(false, parser.matchName("king USMC", "Lee U.S.M.C."));
        assertEquals(true, parser.matchName("reagan USMC", "reagan U.S.M.C."));
        assertEquals(false, parser.matchName("reagan USMC", "reagan U.S.M."));
        assertEquals(true, parser.matchName("reagan USMC", "reagan USMC"));

    }

    @Test
    public void test_USA_R_Suffixes() {
        assertEquals(false, parser.matchName("walker USA", "walker USAR"));
        assertEquals(false, parser.matchName("walker USAR", "walker USA"));
        assertEquals(false, parser.matchName("walker U.S.A.", "walker USAR"));
        assertEquals(false, parser.matchName("walker U.S.A.R.", "walker USA"));
        assertEquals(false, parser.matchName("walker USA", "walker U.S.A.R."));
        assertEquals(false, parser.matchName("walker USAR", "walker U.S.A."));
        assertEquals(false, parser.matchName("walker U.S.A.", "walker U.S.A.R."));
        assertEquals(false, parser.matchName("walker U.S.A.R.", "walker U.S.A."));
        assertEquals(true, parser.matchName("walker USA", "walker USA"));
        assertEquals(true, parser.matchName("walker USAR", "walker USAR"));

    }

    @Test
    public void testGetFirstName() {
        assertEquals("Smith", parser.getFirstName("Mr. Smith"));
        assertEquals("Smith", parser.getFirstName("Mrs. Smith"));
        assertEquals("Smith", parser.getFirstName("Dr. Smith"));
        assertEquals("Smith", parser.getFirstName("dr Smith"));
        assertEquals("Miss.", parser.getFirstName("Miss. Smith"));
        assertEquals("Smith", parser.getFirstName("Ms. Smith"));
        assertEquals("Senor", parser.getFirstName("Senor Smith"));
        assertEquals("Professor", parser.getFirstName("Professor Smith"));
        assertEquals("Adam", parser.getFirstName("Prof Adam Mark Smith Third"));
        assertEquals("Ms.Smith", parser.getFirstName("Ms.Smith"));

        assertEquals("Smith", parser.getFirstName("		Ms. Smith"));
        assertEquals("Smith", parser.getFirstName("		Ms. 		Smith"));
        assertEquals("Smith", parser.getFirstName("		Ms. Smith		"));

        assertEquals("Smith", parser.getFirstName("		Ms. Smith Jones Jr"));
        assertEquals("Smith", parser.getFirstName("		Ms. 		Smith	Adam"));
        assertEquals("Smith", parser.getFirstName("		Ms. Smith		Smithsonian"));
    }


    @Test
    public void testCanadaNamesWithSpecialChars() {


        assertEquals(true, parser.matchName("O'Donnel", "ODonnel"));
        assertEquals(true, parser.matchName("ODonnel", "O'Donnel"));
        assertEquals(true, parser.matchName("De-Cullies", "DeCullies"));
        assertEquals(true, parser.matchName("Bob Smith", "Bob T. Smith"));
        assertEquals(true, parser.matchName("Bob T. Smith", "Bob T. Smith"));
        assertEquals(true, parser.matchName("Bob Smith T.", "Bob T. Smith"));
        assertEquals(true, parser.matchName("Bob T. Smith", "Bob Smith T."));
        assertEquals(true, parser.matchName("T. Bob Smith", "Bob Smith T."));

        assertEquals(true, parser.matchName(" Junior", "Scott Jr"));
        assertEquals(true, parser.matchName(" O'Hare", "Scott O'Hare"));
        assertEquals(true, parser.matchName("Smith", "Bob T. Smith"));
        assertEquals(true, parser.matchName("William Mendes", "William Mendes"));
        assertEquals(true, parser.matchName("DE-CILLIS", "SALVATORE DE-CILLIS"));
        assertEquals(false, parser.matchName("de_CILLIS", "SALVATORE DE-CILLIS"));

        assertEquals(true, parser.matchName("Donnel", "ODonnel"));
        assertEquals(true, parser.matchName("DeCullies", "Decullies"));
        assertEquals(true, parser.matchName("Cullies", "DeCullies"));

        // Kumar test cases
        assertEquals(true, parser.matchName(" Junior", "Scott Jr"));
        assertEquals(true, parser.matchName(" O'Hare", "Scott O'Hare"));
        assertEquals(true, parser.matchName("Smith", "Bob T. Smith"));
        assertEquals(true, parser.matchName("William Mendes", "William Mendes"));
        assertEquals(true, parser.matchName("DE-CILLIS", "SALVATORE DE-CILLIS"));
        assertEquals(false, parser.matchName("de_CILLIS", "SALVATORE DE-CILLIS"));
        assertEquals(false, parser.matchName("Bob T Smith", " Robert T Smith"));
        assertEquals(true, parser.matchName("van", "Sullivan Jr"));
        assertEquals(false, parser.matchName("Becker Phd", "Becker Phd Sr"));

        //assertEquals(true, parser.matchName("Becker Sr Phd", "Boris Becker Senior Phd")); // 2 suffixes
        assertEquals(true, parser.matchName("O-Sullivan", "O'Sullivan Sr"));


        // Gregory test cases
        assertEquals(true, parser.matchName("Fredo", "John Fredo DO"));
        assertEquals(true, parser.matchName("Stevenson", "John Stevenson Ret"));
        assertEquals(true, parser.matchName("Beth", "Elizabeth McBeth"));
        assertEquals(false, parser.matchName("McBeth", "Elizabeth McBeth-Dachau"));
        assertEquals(false, parser.matchName("John Fredo MD", "John Fredo DO"));
        assertEquals(false, parser.matchName("Johnathon Fredo, MD", "John Fredo DO"));
        assertEquals(true, parser.matchName("Andrettii", "Antonio Andrettii II"));
        assertEquals(true, parser.matchName("Andretii 2nd", "Antonio Andretii ii"));


        // Veena test cases
        assertEquals(true, parser.matchName("van", "Vanvan Jr"));
        assertEquals(false, parser.matchName("De Cullies", "De-Cullies"));
        assertEquals(true, parser.matchName("De Cullies", "DeCullies"));
        assertEquals(true, parser.matchName("DeCullies", "De Cullies"));
        assertEquals(true, parser.matchName("Leo", "Leo-Leo"));
        assertEquals(false, parser.matchName("Leo Leo", "Leo-Leo"));
        assertEquals(true, parser.matchName("Leo", "Leo-Leo")); //???
        assertEquals(true, parser.matchName("Leo", "Leo Leo"));
        assertEquals(true, parser.matchName("LeoLeo", "Leo-Leo"));


        // Deel test cases
        assertEquals(true, parser.matchName("Langworthy", "Jack T. Langworthy II"));
        assertEquals(true, parser.matchName("Langworthy", "Jack T. Langworthy III"));
        assertEquals(true, parser.matchName("Langworthy", "Jack T. Langworthy Sr."));
        assertEquals(true, parser.matchName("Langworthy", "Jack T. Langworthy Jr"));
        assertEquals(true, parser.matchName("Ackerman", "R. W. Ackerman"));
        assertEquals(false, parser.matchName("Leeds", "Lenora M. Leeds CRM"));
        assertEquals(true, parser.matchName("Leeds", "Col. Lenora M. Leeds"));
    }

    @Test
    public void negativeScenarios() {
        assertEquals(true, parser.matchName("Susan Stephanie O'Malley", "O-Malley"));
        assertEquals(true, parser.matchName("O-Sullivan", "O'Sullivan Sr"));
        assertEquals(true, parser.matchName("De-Cullies", "Betty DeDe De Cullies"));
        assertEquals(true, parser.matchName("De Cullies", "DeCullies"));
        assertEquals(true, parser.matchName("DeCullies", "De-Cullies"));
        assertEquals(true, parser.matchName("DeCullies", "Betty De Cullies"));
        assertEquals(false, parser.matchName("Amit Sethi", "Sethi Amit"));

        assertEquals(false, parser.matchName("Lam", "John Lam Doe"));
        assertEquals(true, parser.matchName("Lam Doe", "John Lam Doe"));
        assertEquals(true, parser.matchName("Doe", "John Lam Doe"));
        assertEquals(false, parser.matchName("John", "John Lam Doe"));

        assertEquals(true, parser.matchName("John O. Malley", "John Malley"));
        assertEquals(true, parser.matchName("John O Malley", "John Malley"));
        assertEquals(false, parser.matchName("John O'Malley", "John Malley"));
        assertEquals(false, parser.matchName("John O'Malley", "John O. Malley"));

        assertEquals(true, parser.matchName("John O'Malley", "John Omalley"));
        assertEquals(false, parser.matchName("John O Malley", "John O'Malley"));

    }


}
