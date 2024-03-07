public class Tester {
    public static void main(String[] args) {
        Crossword crossword = new Crossword();
        System.out.println(crossword);
        crossword.generate();
        System.out.println(crossword);
    }
}
