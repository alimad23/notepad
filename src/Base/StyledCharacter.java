package Base;



public class StyledCharacter {
    private char c;
    private boolean hasValue = false;
    private boolean selected = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public StyledCharacter(char c) {
        this.c = c;
    }

    public char toChar() {
        return c;
    }

    public void setHasValue(boolean hasValue) {
        this.hasValue = hasValue;
    }

    public boolean isHasValue() {
        return hasValue;
    }

}
