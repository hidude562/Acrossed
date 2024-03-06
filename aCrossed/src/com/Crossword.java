import java.util.ArrayList;

public class Crossword {

    private Character[][] crossword;

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
        try {
            crossword[0][0].setCharacter((char) 60);
        } catch(Exception e) {

        }
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
            word += crossword[y][x2].getCharacter();
        }

        words[0] = word;

        word = "";

        for(int y2 = 0; y2 < crossword.length; y2++) {
            word += crossword[y2][x].getCharacter();
        }

        words[1] = word;

        return words;
    }

    public void generate() {
        ArrayList<Character> iterations = getIterationSequence();

        for(var i = 0; i < iterations.size(); i++) {

        }
    }
}
