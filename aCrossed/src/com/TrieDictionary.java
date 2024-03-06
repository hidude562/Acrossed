import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class TrieNode {
    TrieNode[] children = new TrieNode[26]; // Assuming only lowercase English letters
    boolean isEndOfWord;

    public TrieNode() {
        isEndOfWord = false;
        for (int i = 0; i < 26; i++) {
            children[i] = null;
        }
    }
}

class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Function to insert a word into the trie
    public void insert(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
    }

    // Function to check if a word is present in the trie
    public boolean search(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (current.children[index] == null) {
                return false;
            }
            current = current.children[index];
        }
        return current.isEndOfWord;
    }

    // Function to collect all words in the trie that start with the given prefix
    public List<String> findWordsWithPrefix(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode current = root;

        // Navigate to the end of the prefix
        for (int i = 0; i < prefix.length(); i++) {
            int index = prefix.charAt(i) - 'a';
            if (current.children[index] == null) {
                return results; // No words with the given prefix
            }
            current = current.children[index];
        }

        // Perform DFS to collect all words starting with the prefix
        dfs(current, new StringBuilder(prefix), results);
        return results;
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
}

public class TrieDictionary {

    public Trie trie;
    public TrieDictionary() {
        this.trie = new Trie();
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


        // Find and print all words with the given prefix
        /*
        List<String> wordsWithPrefix = trie.findWordsWithPrefix("prim"); // Replace "pre" with any prefix
        for (String word : wordsWithPrefix) {
            System.out.println(word);
        }
        */
    }
}