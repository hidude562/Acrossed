import java.util.ArrayList;

public class Crossword {

    private Character[][] crossword;
    private TrieDictionary dictionary;

    public Crossword() {
        /*
        Typical NYT 5x5 crossword configuration by default

        # X X X X
        X X X X X
        X X X X X
        X X X X X
        X X X X #

        */

        crossword = new Character[5][5];
        for(int y = 0; y < crossword.length; y++) {
            for(int x = 0; x < crossword[0].length; x++) {
                crossword[y][x] = new Character(false, (char) 97);
            }
        }
        try {
            crossword[0][0].setIsBlocked(true);
            crossword[crossword.length - 1][crossword[0].length - 1].setIsBlocked(true);
        } catch(Exception e) {

        }

        dictionary = new TrieDictionary();
    }

    private ArrayList<Character> getIterationSequence() {
        // Reference to the crossword array
        ArrayList<Character> iterations = new ArrayList<Character>();

        // Save the patterns (Refer to readme)

        int x = 1;
        int y = 0;

        for(int i = 0; i < crossword[0].length; i++) {
            x = i;
            y = 0;

            for(int j = 0; j < i * 2; j++) {
                iterations.add(crossword[y][x]);

                int temp = x;
                x = y;
                y = temp;

                if(j%2 == 1)
                    y++;


            }

            x = i;
            y = i;
            iterations.add(crossword[y][x]);
        }

        return iterations;
    }

    public String[] getWordsAtPos(int x, int y) {
        String[] words = new String[2];

        String word = "";

        for(int x2 = 0; x2 < crossword.length; x2++) {
            char character = crossword[y][x2].getCharacter();
            if((int) character != 0)
                word += character;
        }

        words[0] = word;

        word = "";

        for(int y2 = 0; y2 < crossword.length; y2++) {
            char character = crossword[y2][x].getCharacter();
            if((int) character != 0)
                word += character;
        }

        words[1] = word;

        return words;
    }

    public void generate() {
        ArrayList<Character> iterations = getIterationSequence();

        String[] words = getWordsAtPos(0,1);
        for(String x : words) {
            System.out.println(x);
        }

        for(var i = 0; i < iterations.size(); i++) {

        }
    }

    public String toString() {
        String str = "";
        for(Character[] y : crossword) {
            for(Character x : y) {
                str+=x.toString() + " ";
            }
            str+="\n";
        }
        return str;
    }
}
