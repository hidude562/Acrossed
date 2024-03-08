import java.util.ArrayList;

public class Tester {
    public static void main(String[] args) {


        Crossword crossword = new Crossword(5);
        System.out.println(crossword);


        long startTime = System.nanoTime();
        crossword.generate(false);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        System.out.println(crossword);
        System.out.println((double) duration / 1000000.0 + " ms");




        /*
        TrieDictionary dictionary = new TrieDictionary();
        System.out.println(dictionary.trie.getLettersByOccurrence(new Word("",5)));
        */
    }
}
