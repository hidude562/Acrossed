public class Character {
    private char character;
    private boolean isOccupied;
    private boolean isBlocked;
    private int tryNumber;

    public Character(boolean blocked) {
        this.isOccupied = blocked;
        this.isBlocked = blocked;
        this.character = (char) 0;
    }
    public Character(Character character) {
        this.character = character.getCharacter();
        this.isOccupied = character.getIsOccupied();
        this.isBlocked = character.getIsBlocked();
        this.tryNumber = character.getTryNumber();
    }

    public Character(boolean blocked, char character) {
        this.isOccupied = blocked;
        this.isBlocked = blocked;
        this.character = character;
    }

    public void setCharacter (char character) throws IllegalAccessException {
        if(isBlocked)
            throw new IllegalAccessException("Tried to set a blocked space to be a character!");

        this.character = character;
        this.isOccupied = true;
        tryNumber++;
    }
    public void setTryNumber(int num) {
        this.tryNumber = num;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean value) {
        isBlocked = value;
        isOccupied = isBlocked;
    }
    // Reset the characters, but not the try number
    public void clearOccupation() {
        if(!this.isBlocked) {
            this.character = (char) 0;
            this.isOccupied = false;
        }
    }

    public void clear() {
        if(!this.isBlocked) {
            this.character = (char) 0;
            this.isOccupied = false;
            tryNumber = 0;
        }
    }

    public int getTryNumber() {
        return tryNumber;
    }

    public boolean getIsOccupied() {
        return isOccupied;
    }

    public char getCharacter() {
        if(isBlocked)
            return (char) 0;
        return character;
    }

    public String toString() {
        if(isBlocked)
            return "#";
        return String.valueOf(this.character);
    }
}
