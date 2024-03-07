public class Word {
    public String value;
    public int length;
    public Word(String value, int length) {
        this.value = value;
        this.length = length;
    }
    public String toString() {
        return value + " : " + length;
    }
}