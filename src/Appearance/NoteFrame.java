package Appearance;

import Base.ScreenText;
import Base.StyledCharacter;
import Base.TextLine;
import KeyListener.NoteKeyListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class NoteFrame extends JFrame {

    private ScreenText screenText;
    private Blinker blinker;
    private NoteKeyListener listener;
    public final static int LINE_BEGINNER = 40;//start pixel
    private boolean showLines = false;
    private Set<JLabel> labels;
    private boolean saved = false;
    private String path;
    private ScreenText backup;


    public NoteFrame() throws InterruptedException {
        screenText = new ScreenText(this);
        listener = new NoteKeyListener(this);
        this.setBounds(200, 100, 800, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(listener);
        this.setTitle("Notepad");
        this.setResizable(false);
        blinker = new Blinker(this);
        setFocusTraversalKeysEnabled(false);
        labels = new HashSet<>();

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int y = 50;
        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            String temp = screenText.getTextLines().get(i).toString();
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            g.drawString(temp, LINE_BEGINNER, y);
            try {
                blinker.blink(this.getGraphics());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            y += 20;
        }
        int selectAll = 1;
        select(this.getGraphics());
        boolean flag = false;
        a:
        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                flag = true;
                if (!screenText.getTextLines().get(i).getStyledCharacters().get(j).isSelected()) {
                    selectAll = -1;
                    break a;
                }
            }
        }

        if (selectAll == 1 && flag) screenText.setSelectAll(true);

        lineNumbers();

    }

    public void removePaint(int index) {
        try {
            int i = screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() - 1;
            if (index > i + 1) {
                screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().remove(i);
                this.repaint();
            } else {
                screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().remove(index - 1);
                this.repaint();
            }
        } catch (Exception e) {
            if (screenText.getTextLineNumber() != 0) {
//                screenText.getTextLines().remove(screenText.getTextLineNumber());
                ArrayList<StyledCharacter> temp = new ArrayList<>();
                if (blinker.index() != screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size()) {
                    for (int i = index; i < screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size(); i++) {
                        temp.add(new StyledCharacter(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).toChar()));
                    }
                }
                screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);

                blinker.setY(blinker.getY() - 20);
                int i = screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size();

                blinker.setX(LINE_BEGINNER + (i + 1) * 7);//age beham khord i+1 bayad i beshe
                for (int j = 0; j < temp.size(); j++) {
//                    System.out.println(temp.get(j).toChar());
                    System.out.println(blinker.index());
                    screenText.addCharacter(temp.get(j), blinker.index() + j);

                }
                screenText.getTextLines().remove(screenText.getTextLineNumber() + 1);
                this.repaint();

            }
//            System.err.println("Line is Empty");
        }
    }//backspace remove

    public ScreenText getScreenText() {
        return screenText;
    }

    public Blinker getBlinker() {
        return blinker;
    }

    public void delete() {//this is for delete key
        int i = blinker.index();
        screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().remove(i);
        this.repaint();
    }

    public void removeSelected() {

//        if (screenText.isSelectAll()){
//            screenText.getTextLines().clear();
//            screenText.getTextLines().add(0,new TextLine());
//            blinker.setX(LINE_BEGINNER);
//            blinker.setY(50);
//            screenText.setTextLineNumber(0);
//            screenText.setSelectAll(false);
//            this.repaint();
//            return;
//        }
        backup = new ScreenText();
        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            backup.getTextLines().add(i, new TextLine());
            for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                backup.getTextLines().get(i).getStyledCharacters().add(new StyledCharacter(screenText.getTextLines().get(i).getStyledCharacters().get(j).toChar()));
                backup.getTextLines().get(i).getStyledCharacters().get(j).setHasValue(true);
                if (screenText.getTextLines().get(i).getStyledCharacters().get(j).isSelected()) {
                    backup.getTextLines().get(i).getStyledCharacters().get(j).setSelected(true);
                }
            }

        }
        int x = 0;
        int y = 0;
        a:
        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                if (screenText.getTextLines().get(i).getStyledCharacters().get(j).isSelected()) {
                    x = j * 7 + LINE_BEGINNER;
                    y = 50 + i * 20;
                    screenText.setTextLineNumber(i);
                    break a;
                }
            }
        }
        int line = 0;
        int index = 0;
        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                if (screenText.getTextLines().get(i).getStyledCharacters().get(j).isSelected()) {
                    line = i;
                    index = j;
                }
            }
        }
        boolean flag = false;
        if (screenText.getTextLines().get(line).getStyledCharacters().size() - 1 != index && (x - LINE_BEGINNER) / 7 != 0 && line != (y - 50) / 20) {

            flag = true;
        }
        ArrayList<StyledCharacter> temp = new ArrayList<>();


        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            screenText.getUndo().add(i, new TextLine());
            for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                if (screenText.getTextLines().get(i).getStyledCharacters().get(j).isSelected()) {
                    screenText.getTextLines().get(i).getStyledCharacters().get(j).setSelected(false);
                    screenText.getUndo().get(i).getStyledCharacters().add(new StyledCharacter(screenText.getTextLines().get(i).getStyledCharacters().get(j).toChar()));
                    screenText.getTextLines().get(i).getStyledCharacters().remove(j);
                    j--;
                }
            }

        }

        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            if (screenText.getTextLines().get(i).getStyledCharacters().size() == 0) {
                screenText.getTextLines().remove(i);
                i--;
            }
        }
        screenText.setTextLineNumber((y - 50) / 20);
        if (flag) {
            if (screenText.getTextLineNumber() != screenText.getTextLines().size() - 1) {
                for (int i = 0; i < screenText.getTextLines().get(screenText.getTextLineNumber() + 1).getStyledCharacters().size(); i++) {
                    temp.add(new StyledCharacter(screenText.getTextLines().get(screenText.getTextLineNumber() + 1).getStyledCharacters().get(i).toChar()));
                }


                for (int i = 0; i < temp.size(); i++) {
                    screenText.addCharacter(temp.get(i), screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size());

                }
                screenText.getTextLines().remove(screenText.getTextLineNumber() + 1);

            }
        }


        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            if (screenText.getTextLines().get(i).getStyledCharacters().size() == 0) {
                screenText.getTextLines().remove(i);
                i--;
            }
        }
        if (screenText.getTextLines().size() == 0) {
            screenText.getTextLines().add(0, new TextLine());
            screenText.setTextLineNumber(0);
        }
//        System.out.println(x);
        blinker.setX(x);
        blinker.setY(y);
        if ((y - 50) / 20 == screenText.getTextLines().size()) {
            screenText.getTextLines().add(screenText.getTextLines().size(), new TextLine());
        }


        this.repaint();

    }//remove selected characters

    public void select(Graphics g) {
        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                if (screenText.getTextLines().get(i).getStyledCharacters().get(j).isSelected()) {
                    g.setColor(Color.blue);
                    g.fillRect(LINE_BEGINNER + 7 * j, 40 + 20 * i, 8, 17);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                    g.drawString(screenText.getTextLines().get(i).getStyledCharacters().get(j).toChar() + "", LINE_BEGINNER + 7 * j, 50 + 20 * i);
                }
            }
        }
//        this.repaint();


    }//draw a fill rect on selected characters

    public void lineNumbers() {
//        this.pack();
        if (showLines) {
            for (int i = 0; i < screenText.getTextLines().size(); i++) {

                JLabel label = new JLabel((i + 1) + "");
                label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                label.setBounds(10, 10 + i * 20, 20, 20);
                labels.add(label);
                this.add(label);
//                System.out.println(label.getText());

            }
        } else {
            Iterator i = labels.iterator();
            while (i.hasNext()) {
                JLabel label = (JLabel) i.next();
                label.setVisible(false);
            }
        }
//        this.repaint();


    }//show the line numbers

    public boolean isShowLines() {
        return showLines;
    }

    public void setShowLines(boolean showLines) {
        this.showLines = showLines;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ScreenText getBackup() {
        return backup;
    }

    public void setBackup(ScreenText backup) {
        this.backup = backup;
    }//for ctrl + z
}
