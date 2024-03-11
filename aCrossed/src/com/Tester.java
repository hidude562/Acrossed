import java.util.ArrayList;

public class Tester {
    public static void main(String[] args) {


        Crossword crossword = new Crossword(15);
        System.out.println(crossword);


        Word[] words = crossword.getWordsAtPos(0,3);
        System.out.println(words[0] + "\n" + words[1]);
        System.out.println((new TrieDictionary()).trie.getHighestMinimumOccurrences(words[0],words[1]));




        long startTime = System.nanoTime();
        crossword.generate(true);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        System.out.println(crossword);
        System.out.println((double) duration / 1000000.0 + " ms");

        TrieDictionary dictionary = new TrieDictionary();
        System.out.println(dictionary.trie.getHighestMinimumOccurrences(new Word("a",2),new Word("",3)));
        System.out.println(dictionary.trie.getLettersByOccurrence(new Word("",3)));


    }
}
