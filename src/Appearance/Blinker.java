package Appearance;

import java.awt.*;

public class Blinker {
    public final static char CURSOR = '|';
    NoteFrame frame;

    private int x = frame.LINE_BEGINNER;
    private int y = 50;

    public Blinker(NoteFrame frame) {
        this.frame = frame;
    }

    public void blink(Graphics g) throws InterruptedException {
        g.setColor(Color.black);
        g.drawString(CURSOR + "", x - 1, y);
    }

    public int index() {
        return (frame.getBlinker().getX() - frame.LINE_BEGINNER) / 7;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int styledCharactersSize(){
        return frame.getScreenText().getTextLines().get(frame.getScreenText().getTextLineNumber()).getStyledCharacters().size();
    }

}
