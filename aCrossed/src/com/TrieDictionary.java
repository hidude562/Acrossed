import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;
    int wordCount;

    public TrieNode(int alphabetSize) {
        children = new TrieNode[alphabetSize];
        isEndOfWord = false;
        wordCount = 0;
    }
}

class Trie {
    private TrieNode root;
    private final int maxWordLength; // Define the maximum word length the trie will support

    public Trie(int maxWordLength) {
        this.maxWordLength = maxWordLength;
        root = new TrieNode(maxWordLength + 1); // +1 to accommodate 0-indexing
    }

    // Function to insert a word into the trie
    public void insert(String word) {
        if (word.length() >= maxWordLength) {
            throw new IllegalArgumentException("Word exceeds maximum allowed length.");
        }
        TrieNode current = root.children[word.length()];
        if (current == null) {
            current = new TrieNode(26); // Assuming only lowercase English letters
            root.children[word.length()] = current;
        }

        // Increment the word count for the length node
        current.wordCount++;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (current.children[index] == null) {
                current.children[index] = new TrieNode(26);
            }
            current = current.children[index];
            current.wordCount++; // Increment the word count for each node in the path
        }

        current.isEndOfWord = true;
    }



    // Function to check if a word is present in the trie
    public boolean search(Word word) {
        TrieNode current = root.children[word.value.length()];
        for (int i = 0; i < word.value.length(); i++) {
            int index = word.value.charAt(i) - 'a';
            if (current.children[index] == null) {
                return false;
            }
            current = current.children[index];
        }
        return current.isEndOfWord;
    }

    // Function to collect all words in the trie that start with the given prefix
    public List<String> findWordsWithPrefix(Word prefix) {
        List<String> results = new ArrayList<>();
        TrieNode current = root.children[prefix.length];

        // Navigate to the end of the prefix
        for (int i = 0; i < prefix.value.length(); i++) {
            int index = prefix.value.charAt(i) - 'a';
            if (current.children[index] == null) {
                return results; // No words with the given prefix
            }
            current = current.children[index];
        }

        // Perform DFS to collect all words starting with the prefix
        dfs(current, new StringBuilder(prefix.value), results);
        return results;
    }

    // Function to get the number of words with the given prefix
    public int countWordsWithPrefix(Word prefix) {
        TrieNode current = root.children[prefix.length];
        for (int i = 0; i < prefix.value.length(); i++) {
            int index = prefix.value.charAt(i) - 'a';
            if (current.children[index] == null) {
                return 0; // No words with the given prefix
            }
            current = current.children[index];
        }
        return current.wordCount;
    }

    // Helper method to perform DFS
    private void dfs(TrieNode node, StringBuilder prefixBuilder, List<String> results) {
        if (node.isEndOfWord) {
            results.add(prefixBuilder.toString());
        }
        for (int i = 0; i < node.children.length; i++) {
            if (node.children[i] != null) {
                char nextChar = (char) (i + 'a');
                prefixBuilder.append(nextChar);
                dfs(node.children[i], prefixBuilder, results);
                prefixBuilder.deleteCharAt(prefixBuilder.length() - 1); // Backtrack
            }
        }
    }

    // Function to get the list of letters following the prefix, sorted by occurrence
    public ArrayList<CharacterCountPair> getLettersByOccurrence(Word prefix) {
        TrieNode current = root.children[prefix.length];
        ArrayList<CharacterCountPair> letters = new ArrayList<>();

        // Navigate to the end of the prefix
        for (int i = 0; i < prefix.value.length(); i++) {
            int index = prefix.value.charAt(i) - 'a';
            if (current.children[index] == null) {
                return letters; // No words with the given prefix
            }
            current = current.children[index];
        }

        // Collect all immediate children and their word counts
        for (int i = 0; i < current.children.length; i++) {
            if (current.children[i] != null) {
                letters.add(new CharacterCountPair((char) (i + 'a'), current.children[i].wordCount));
            }
        }

        // Sort the list based on word count in descending order
        Collections.sort(letters, (pair1, pair2) -> pair2.count - pair1.count);

        return letters;
    }

    // Helper class to store the character and its word count
    static class CharacterCountPair {
        char character;
        int count;

        CharacterCountPair(char character, int count) {
            this.character = character;
            this.count = count;
        }

        @Override
        public String toString() {
            return character + ":" + count;
        }
    }

    // Method to find the characters with the highest minimum occurrence between two prefixes
    public ArrayList<CharacterCountPair> getHighestMinimumOccurrences(Word prefix1, Word prefix2) {
        // Get the lists of letters by occurrence for both prefixes
        ArrayList<CharacterCountPair> list1 = getLettersByOccurrence(prefix1);
        ArrayList<CharacterCountPair> list2 = getLettersByOccurrence(prefix2);

        // Create maps to store the counts for quick lookup
        Map<java.lang.Character, Integer> counts1 = new HashMap<>();
        Map<java.lang.Character, Integer> counts2 = new HashMap<>();
        for (CharacterCountPair pair : list1) {
            counts1.put(pair.character, pair.count);
        }
        for (CharacterCountPair pair : list2) {
            counts2.put(pair.character, pair.count);
        }

        // Find the characters with the highest minimum occurrence
        ArrayList<CharacterCountPair> result = new ArrayList<>();
        for (java.lang.Character c : counts1.keySet()) {
            if (counts2.containsKey(c)) {
                int minCount = Math.min(counts1.get(c), counts2.get(c));
                result.add(new CharacterCountPair(c, minCount));
            }
        }

        // Sort the result based on count in descending order
        Collections.sort(result, (pair1, pair2) -> pair2.count - pair1.count);

        return result;
    }
}

public class TrieDictionary {
    public Trie trie;
    public TrieDictionary() {
        this.trie = new Trie(30);
        String fileName = "dictionary.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each line in the file is a word
                trie.insert(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}