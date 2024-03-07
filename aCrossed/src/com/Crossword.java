import java.util.ArrayList;

class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

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
                crossword[y][x] = new Character(false);
            }
        }
        try {

            crossword[0][0].setIsBlocked(true);
            crossword[crossword.length - 1][crossword[0].length - 1].setIsBlocked(true);

            crossword[1][0].setCharacter((char) 97);
            crossword[0][1].setCharacter((char) 98);

            /*
            crossword[1][0].setCharacter((char) 98);
            crossword[2][0].setCharacter((char) 99);
            crossword[3][0].setCharacter((char) 100);
            */
        } catch(Exception e) {

        }

        dictionary = new TrieDictionary();
    }

    private ArrayList<Point> getIterationSequence() {
        // Reference to the crossword array
        ArrayList<Point> iterations = new ArrayList<Point>();

        // Save the patterns (Refer to readme)

        int x = 1;
        int y = 0;

        for(int i = 0; i < crossword[0].length; i++) {
            x = i;
            y = 0;

            for(int j = 0; j < i * 2; j++) {
                iterations.add(new Point(x,y));

                int temp = x;
                x = y;
                y = temp;

                if(j%2 == 1)
                    y++;


            }

            x = i;
            y = i;
            iterations.add(new Point(x,y));
        }

        return iterations;
    }

    public Word[] getWordsAtPos(int x, int y) {
        Word[] words = new Word[2];

        /*
        TODO: when i implement crossword maps with non-uniform maps or whatever
        This will need to start at the character
        then go one left, append at beggining
        go to right 2, append at end
        and then do this until one word barrier is met
        then go as far as before to the otherside then yea

        this should also be faster since it goes like the minimum cells
        */
        {
            String word = "";
            int wordLen = 0;
            for (int x2 = 0; x2 < crossword.length; x2++) {
                char character = crossword[y][x2].getCharacter();
                boolean isBlocked = crossword[y][x2].getIsBlocked();
                if(!isBlocked)
                    wordLen++;

                if ((int) character != 0)
                    word += character;
            }

            words[0] = new Word(word, wordLen);
        }
        {
            String word = "";
            int wordLen = 0;
            for (int y2 = 0; y2 < crossword.length; y2++) {
                char character = crossword[y2][x].getCharacter();
                boolean isBlocked = crossword[y2][x].getIsBlocked();
                if(!isBlocked)
                    wordLen++;

                if ((int) character != 0)
                    word += character;
            }

            words[1] = new Word(word, wordLen);
        }

        return words;
    }

    // Generates until success, or failure
    public boolean generate() {
        ArrayList<Point> iterations = getIterationSequence();

        // Skip first 3 because they are already set
        int i = 3;
        while(i < iterations.size()) {
            // In theory, with this algorithm, you will never need the right and down positions
            // Because it only expands one way
            if(!crossword[iterations.get(i).y][iterations.get(i).x].getIsBlocked()) {
                Word[] words = getWordsAtPos(iterations.get(i).x, iterations.get(i).y);
                // TODO: work with cases of only one word intersection

                ArrayList<Trie.CharacterCountPair> highestMinOccurrences =
                        dictionary.trie.getHighestMinimumOccurrences(words[0], words[1]);

                try {
                    Character currentCrosswordItem = crossword[iterations.get(i).y][iterations.get(i).x];
                    currentCrosswordItem.setCharacter(highestMinOccurrences.get(currentCrosswordItem.getTryNumber()).character);
                } catch (Exception e) {
                    // If there are  no common word letters, go back until there's one with two
                    Character currentCrosswordItem = crossword[iterations.get(i).y][iterations.get(i).x];
                    int letterChoiceLength = 0;
                    while (true) {
                        words = getWordsAtPos(iterations.get(i).x, iterations.get(i).y);
                        // TODO: work with cases of only one word intersection

                        highestMinOccurrences = dictionary.trie.getHighestMinimumOccurrences(words[0], words[1]);
                        currentCrosswordItem = crossword[iterations.get(i).y][iterations.get(i).x];

                        letterChoiceLength = highestMinOccurrences.size();

                        if(letterChoiceLength >= currentCrosswordItem.getTryNumber() + 1) {
                            i--;
                            break;
                        } else {
                            currentCrosswordItem.clear();
                            i--;
                        }
                    }
                }

            }

            i++;
        }

        return true;
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
