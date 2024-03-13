import java.util.ArrayList;
import java.lang.Runnable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Crossword{
    public Character[][] getCrossword() {
        return crossword;
    }
    public void setCrossword(Character val, int x, int y) {
        crossword[y][x] = val;
    }

    public TrieDictionary getDictionary() {
        return dictionary;
    }

    public int getIter() {
        return iter;
    }
    public int getForkNumber() {
        return forkNumber;
    }
    public int getTotalIterations() {
        return totalIterations;
    }

    public int getHighestIter() {
        return highestIter;
    }

    public ArrayList<Point> getIterations() {
        return iterations;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public ArrayList<Crossword> getCrosswordForkCanidates() {
        return crosswordForkCanidates;
    }

    private Character[][] crossword;
    private TrieDictionary dictionary;
    private int iter = 0;
    private int totalIterations = 0;
    private int highestIter = -1;
    ArrayList<Point> iterations;
    private boolean isComplete = false;
    private ArrayList<Crossword> crosswordForkCanidates;
    private int forkNumber = 0;

    public Crossword(int size) {
        /*
        Typical NYT 5x5 crossword configuration by default

        # X X X X
        X X X X X
        X X X X X
        X X X X X
        X X X X #

        */

        crossword = new Character[size][size];
        for(int y = 0; y < crossword.length; y++) {
            for(int x = 0; x < crossword[0].length; x++) {
                crossword[y][x] = new Character(false);
            }
        }

        crosswordForkCanidates = new ArrayList<Crossword>();

        try {
            if(size==15) {
                crossword[4][0].setIsBlocked(true);
                crossword[9][0].setIsBlocked(true);
                crossword[4][1].setIsBlocked(true);
                crossword[9][1].setIsBlocked(true);
                crossword[4][2].setIsBlocked(true);
                crossword[3][3].setIsBlocked(true);
                crossword[6][5].setIsBlocked(true);
                crossword[5][6].setIsBlocked(true);
                crossword[4][7].setIsBlocked(true);
                crossword[3][9].setIsBlocked(true);
                crossword[2][10].setIsBlocked(true);
                crossword[1][10].setIsBlocked(true);
                crossword[0][10].setIsBlocked(true);
                crossword[5][14].setIsBlocked(true);
                crossword[5][13].setIsBlocked(true);

                crossword[14][4].setIsBlocked(true);
                crossword[13][4].setIsBlocked(true);
                crossword[12][4].setIsBlocked(true);
                crossword[11][5].setIsBlocked(true);
                crossword[10][7].setIsBlocked(true);
                crossword[9][8].setIsBlocked(true);
                crossword[8][9].setIsBlocked(true);
                crossword[11][11].setIsBlocked(true);
                crossword[10][12].setIsBlocked(true);
                crossword[10][13].setIsBlocked(true);
                crossword[10][14].setIsBlocked(true);
            } else if(size == 7) {
                crossword[3][0].setIsBlocked(true);
                crossword[0][3].setIsBlocked(true);
                crossword[6][3].setIsBlocked(true);
                crossword[3][6].setIsBlocked(true);
            } else if(size==5 || size==6) {
                crossword[0][0].setIsBlocked(true);
                crossword[crossword.length - 1][crossword[0].length - 1].setIsBlocked(true);
            }


            /*
            for(int i = 0; i < 10; i++) {
                int randX = (int) (Math.random() * 15);
                int randY = (int) (Math.random() * 15);
                if(!crossword[randY][randX].getIsBlocked()) {
                    crossword[randY][randX].setIsBlocked(true);
                    //crossword[randY][randX].setCharacter((char) (97 + ((int) (Math.random() * 25))));
                }
            }
             */


            /*
            crossword[1][0].setCharacter((char) 104);
            crossword[0][1].setCharacter((char) 98);
            */



        } catch(Exception e) {

        }

        dictionary = new TrieDictionary();
        iterations = getIterationSequence();
    }

    public Crossword(Crossword original) {
        this.crossword = new Character[original.crossword.length][original.crossword[0].length];
        for(int y = 0; y < original.crossword.length; y++) {
            for(int x = 0; x < original.crossword[0].length; x++) {
                this.crossword[y][x] = new Character(original.getCrossword()[y][x]);
            }
        }

        this.dictionary = original.getDictionary();
        this.iter = original.getIter();
        this.totalIterations = original.getTotalIterations();
        this.highestIter = original.getHighestIter();
        this.iterations = original.getIterations();
        this.isComplete = original.getIsComplete();
        this.crosswordForkCanidates = new ArrayList<Crossword>();
        this.forkNumber = original.getForkNumber() + 1;
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
        This will need to start at the character
        then go one left, append at beggining
        go to right 2, append at end
        and then do this until one word barrier is met
        then go as far as before to the otherside then yea

        this should also be faster since it goes like the minimum cells
        */
        {
            String word = "";
            int leftOffset = 0;
            int rightOffset = 0;
            int wordLen = 1;
            boolean canMoveLeft = true;
            boolean canMoveRight = true;

            while (canMoveLeft || canMoveRight) {
                // Move left
                if (canMoveLeft) {
                    int newX = x - leftOffset - 1;
                    if (newX >= 0 && !crossword[y][newX].getIsBlocked()) {
                        char character = crossword[y][newX].getCharacter();
                        if (character != 0) {
                            word = character + word;
                        }
                        wordLen++;
                        leftOffset++;
                    } else {
                        canMoveLeft = false;
                    }
                }

                // Move right
                if (canMoveRight) {
                    int newX = x + rightOffset + 1;
                    if (newX < crossword[y].length && !crossword[y][newX].getIsBlocked()) {
                        char character = crossword[y][newX].getCharacter();
                        /* There will never be characters to the right
                        if (character != 0) {
                            word += character;
                        }
                         */
                        wordLen++;
                        rightOffset++;
                    } else {
                        canMoveRight = false;
                    }
                }
            }

            words[0] = new Word(word, wordLen);
        }
        {
            String word = "";
            int upOffset = 0;
            int downOffset = 0;
            int wordLen = 1;
            boolean canMoveUp = true;
            boolean canMoveDown = true;

            while (canMoveUp || canMoveDown) {
                // Move Up
                if (canMoveUp) {
                    int newY = y - upOffset - 1;
                    if (newY >= 0 && !crossword[newY][x].getIsBlocked()) {
                        char character = crossword[newY][x].getCharacter();

                        if (character != 0) {
                            word = character + word;
                        }
                        wordLen++;
                        upOffset++;
                    } else {
                        canMoveUp = false;
                    }
                }

                // Move right
                if (canMoveDown) {
                    int newY = y + downOffset + 1;
                    if (newY < crossword.length && !crossword[newY][x].getIsBlocked()) {
                        char character = crossword[newY][x].getCharacter();
                        /* There will never be characters to the right
                        if (character != 0) {
                            word += character;
                        }
                        */
                        wordLen++;
                        downOffset++;
                    } else {
                        canMoveDown = false;
                    }
                }
            }

            words[1] = new Word(word, wordLen);
        }

        return words;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void iterate(boolean debug) {
        System.out.println("ITER");
        if(iter >= iterations.size()) {
            this.isComplete = true;
            return;
        }

        // In theory, with this algorithm, you will never need the right and down positions
        // Because it only expands one way
        if(!crossword[iterations.get(iter).y][iterations.get(iter).x].getIsOccupied()) {
            Word[] words = getWordsAtPos(iterations.get(iter).x, iterations.get(iter).y);

            ArrayList<Trie.CharacterCountPair> highestMinOccurrences =
                    dictionary.trie.getHighestMinimumOccurrences(words[0], words[1]);

            // TODO: fork process if like 100 other occurances or so

            try {
                Character currentCrosswordItem = crossword[iterations.get(iter).y][iterations.get(iter).x];
                currentCrosswordItem.setCharacter(highestMinOccurrences.get(currentCrosswordItem.getTryNumber()).character);

                // If it is the first try approaching this square combination, try searching for canidates
                // (note: it checks for equality at 1 instead of zero because setting the character increments the counter by one)
                if(currentCrosswordItem.getTryNumber() == 1) {
                    int occurancesTry = 1;
                    while (occurancesTry < highestMinOccurrences.size()) {
                        int occurances = highestMinOccurrences.get(occurancesTry).count;
                        if (occurances > 1) {
                            Crossword fork = new Crossword(this);
                            Character currentForkItem = fork.crossword[iterations.get(iter).y][iterations.get(iter).x];
                            currentForkItem.clear();
                            currentForkItem.setTryNumber(occurancesTry);
                            this.crosswordForkCanidates.add(fork);
                        } else {
                            break;
                        }
                        occurancesTry++;
                    }
                }
            } catch (Exception e) {
                // If there are  no common word letters, go back until there's one with two
                Character currentCrosswordItem = crossword[iterations.get(iter).y][iterations.get(iter).x];
                int letterChoiceLength = 0;
                iter--;

                while (true) {
                    words = getWordsAtPos(iterations.get(iter).x, iterations.get(iter).y);

                    // TODO: work with cases of only one word intersection
                    if(!crossword[iterations.get(iter).y][iterations.get(iter).x].getIsBlocked()) {
                        highestMinOccurrences = dictionary.trie.getHighestMinimumOccurrences(words[0], words[1]);

                        currentCrosswordItem = crossword[iterations.get(iter).y][iterations.get(iter).x];
                        int currentOccurancesForTryNum = highestMinOccurrences.get(currentCrosswordItem.getTryNumber() - 1).count;

                        letterChoiceLength = highestMinOccurrences.size();

                        if (letterChoiceLength >= currentCrosswordItem.getTryNumber() + 1) { // && currentOccurancesForTryNum > 0
                            iter--;
                            currentCrosswordItem.clearOccupation();
                            break;
                        } else {
                            currentCrosswordItem.clear();
                            iter--;
                        }
                    } else {
                        iter--;
                    }
                }
            }
            if(debug && (totalIterations < 500 || totalIterations % 200 == 0))
                System.out.println(this);

            totalIterations++;
        }

        if(iter > highestIter)
            highestIter = iter;
        iter++;
    }
    public void runIterationBatch(boolean debug) {
        int iterationBatchSize = 500;
        for(int i = 0; i < iterationBatchSize; i++) {
            iterate(debug);
        }

    }

    // Generates until success, or failure
    public Crossword run(boolean debug, int parentHighestIter) {
        int previousHighestIterInBatch = parentHighestIter;
        runIterationBatch(debug);
        if(highestIter > previousHighestIterInBatch) {
            while(highestIter != previousHighestIterInBatch) {
                previousHighestIterInBatch = highestIter;
                runIterationBatch(debug);
            }
            if(this.isComplete) {
                // If complete, return this and true value stored in here
                System.out.println(this);
                return this;
            }

            ArrayList<CompletableFuture<Crossword>> processes = new ArrayList<CompletableFuture<Crossword>>();
            // Fork any processes
            for(Crossword forkedCrossword : crosswordForkCanidates) {
                System.out.println(this.forkNumber + " Fork number, " + forkedCrossword);
                processes.add(CompletableFuture.supplyAsync(() -> {
                    Crossword crosswordForked = forkedCrossword.run(debug, this.highestIter);
                    return crosswordForked;
                }));
                System.out.println("RUNNING");
            }

            for(int i = 0; i < processes.size(); i++) {
                try {
                    Crossword potentiallyDoneCrossword = processes.get(i).get();
                    if(potentiallyDoneCrossword.getIsComplete()) {
                        return potentiallyDoneCrossword;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            // Quit
        }
        return this;
    }

    public String toString() {
        String str = "";
        for(Character[] y : crossword) {
            for(Character x : y) {
                str+=x.getTryNumber() + " ";
            }
            str+="\n";
        }
        return str;
    }
}
