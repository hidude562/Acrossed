public class Word {
    public String value;
    public int length;
    public Word(String value, int length) {
        this.value = value;
        // Because some dictionaries dont support 1 letter words
        this.length = length;
    }
    public String toString() {
        return value + " : " + length;
    }
}