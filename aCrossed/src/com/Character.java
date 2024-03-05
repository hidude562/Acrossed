public class Character {
    private char character;
    private boolean isOccupied;
    private boolean isBlocked;

    public Character(boolean blocked) {
        this.isOccupied = blocked;
        this.isBlocked = blocked;
    }

    public void setCharacter (char character) throws IllegalAccessException {
        if(isBlocked)
            throw new IllegalAccessException("Tried to set a blocked space to be a character!");

        this.character = character;
        this.isOccupied = true;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public char getCharacter() {
        return character;
    }

    public String toString() {
        if(isBlocked)
            return "#";
        return String.valueOf(this.character);
    }
}