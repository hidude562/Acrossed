import java.util.ArrayList;

public class Tester {
    public static void main(String[] args) {


        Crossword crossword = new Crossword(7);
        System.out.println(crossword);

        /*
        Word[] words = crossword.getWordsAtPos(0,3);
        System.out.println(words[0] + "\n" + words[1]);
        System.out.println((new TrieDictionary()).trie.getHighestMinimumOccurrences(words[0],words[1]));
        */

        long startTime = System.nanoTime();
        Crossword finished = crossword.run(false, -1);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        System.out.println(finished);
        System.out.println(duration / 1000000000.0);
    }
}
