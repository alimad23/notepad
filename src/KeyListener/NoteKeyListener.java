package KeyListener;

import Appearance.NoteFrame;
import Base.ScreenText;
import Base.StyledCharacter;
import Base.TextLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NoteKeyListener implements KeyListener {
    private NoteFrame noteFrame;
    private ScreenText screenText;
    private Set<Integer> keyPressed = new HashSet<>();
    private String fWord;
    private boolean replace = false;
    private String replaceWith;


    public NoteKeyListener(NoteFrame noteFrame) throws InterruptedException {

        this.noteFrame = noteFrame;
        screenText = noteFrame.getScreenText();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 8) {//backspace ascii code = 8
            if (screenText.isSelectAll()) {
                noteFrame.setBackup(new ScreenText());
                for (int i = 0; i < screenText.getTextLines().size(); i++) {
                    noteFrame.getBackup().getTextLines().add(i, new TextLine());
                    for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                        noteFrame.getBackup().getTextLines().get(i).getStyledCharacters().add(new StyledCharacter(screenText.getTextLines().get(i).getStyledCharacters().get(j).toChar()));
                        noteFrame.getBackup().getTextLines().get(i).getStyledCharacters().get(j).setHasValue(true);
                        if (screenText.getTextLines().get(i).getStyledCharacters().get(j).isSelected()) {
                            noteFrame.getBackup().getTextLines().get(i).getStyledCharacters().get(j).setSelected(true);
                        }
                    }
                }
                screenText.getTextLines().clear();
                screenText.getTextLines().add(0, new TextLine());
                screenText.setSelectAll(false);
                screenText.setTextLineNumber(0);
                noteFrame.repaint();
                noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                noteFrame.getBlinker().setY(50);
            } else if (screenText.thereIsSelected()) {
                noteFrame.removeSelected();
                screenText.deselect();
            } else {
                noteFrame.setBackup(new ScreenText());
                for (int i = 0; i < screenText.getTextLines().size(); i++) {
                    noteFrame.getBackup().getTextLines().add(i, new TextLine());
                    for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                        noteFrame.getBackup().getTextLines().get(i).getStyledCharacters().add(new StyledCharacter(screenText.getTextLines().get(i).getStyledCharacters().get(j).toChar()));
                        noteFrame.getBackup().getTextLines().get(i).getStyledCharacters().get(j).setHasValue(true);

                    }
                }
                int index = noteFrame.getBlinker().index();
                noteFrame.removePaint(index);
                if (noteFrame.getBlinker().getX() != noteFrame.LINE_BEGINNER) {
                    noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() - 7);
                }
            }
        } else {
            if (screenText.isSelectAll()) {
                if (e.getKeyChar() >= ' ' && e.getKeyChar() <= 126) {//u0001=ctrl+a
                    screenText.getTextLines().clear();
                    screenText.setTextLineNumber(0);
                    noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                    noteFrame.getBlinker().setY(50);
                    StyledCharacter temp = new StyledCharacter(e.getKeyChar());
                    screenText.getTextLines().add(new TextLine());
                    screenText.addCharacter(temp, 0);
                    screenText.setSelectAll(false);
                    noteFrame.repaint();
                    noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER + 7);//7 font !
                }
            } else {
                StyledCharacter temp = new StyledCharacter(e.getKeyChar());
                if (temp.toChar() >= ' ' && temp.toChar() <= 126) {//ctrl+a ctrl+d delete enter
                    if (screenText.thereIsSelected()) noteFrame.removeSelected();
                    int index = noteFrame.getBlinker().index();
                    screenText.addCharacter(temp, index);
                    screenText.deselect();
                    noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() + 7);
                    noteFrame.repaint();

                }
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {

        keyPressed.add(e.getKeyCode());

        if (e.getKeyChar() == '\u0001') {//ctrl+a select All
            screenText.setSelectAll(true);
            for (int i = 0; i < screenText.getTextLines().size(); i++) {
                for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                    screenText.getTextLines().get(i).getStyledCharacters().get(j).setSelected(true);
                }
            }
            noteFrame.select(noteFrame.getGraphics());
        } else if (e.getKeyChar() == '\u0004') {//ctrl+d delete line
            screenText.removeLine();
            noteFrame.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {//right arrow key
            if (keyPressed.size() == 2 && keyPressed.contains(16)) {//shift + right
                if (noteFrame.getBlinker().index() == screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size()) {//vaghti blinker akhare khatte
                    if (screenText.getTextLineNumber() != screenText.getTextLines().size() - 1) {//next line exists
                        noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                        noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);
                        screenText.setTextLineNumber(screenText.getTextLineNumber() + 1);
                    }

                } else {
                    boolean selected = screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(noteFrame.getBlinker().index()).isSelected();
                    screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(noteFrame.getBlinker().index()).setSelected(!selected);
                    noteFrame.select(noteFrame.getGraphics());
                    noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() + 7);

                }
                noteFrame.repaint();


            } else if (keyPressed.size() == 2 && keyPressed.contains(17)) {//ctrl + right
                if (noteFrame.getBlinker().index() == screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size()) {
                    if (screenText.getTextLineNumber() != screenText.getTextLines().size() - 1) {
                        screenText.setTextLineNumber(screenText.getTextLineNumber() + 1);
                        noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                        noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);
                    }
                } else {
                    noteFrame.getBlinker().setX(screenText.findNextSpace() * 7 + noteFrame.LINE_BEGINNER);
                }
                noteFrame.repaint();
            } else if (keyPressed.size() == 3 && keyPressed.contains(16) && keyPressed.contains(17)) {//shift + ctrl + right

                if (noteFrame.getBlinker().index() == screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size()) {
                    if (screenText.getTextLineNumber() != screenText.getTextLines().size() - 1) {
                        screenText.setTextLineNumber(screenText.getTextLineNumber() + 1);
                        noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                        noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);
                    }
                } else {
                    int index = noteFrame.getBlinker().index();
                    noteFrame.getBlinker().setX(screenText.findNextSpace() * 7 + noteFrame.LINE_BEGINNER);
                    for (int i = index; i < noteFrame.getBlinker().index(); i++) {
                        boolean selected = screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).isSelected();
                        screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(!selected);
                    }
                    noteFrame.select(noteFrame.getGraphics());
                }
                noteFrame.repaint();
            } else {//right
                if (noteFrame.getBlinker().index() < screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size()) {
                    if (screenText.isSelectAll()) screenText.setSelectAll(false);
                    noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() + 7);// 7 = length of each character in monospaced mode
                    noteFrame.repaint();
                } else if (noteFrame.getBlinker().index() == screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size()) {
                    if (screenText.getTextLineNumber() != screenText.getTextLines().size() - 1) {
                        noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                        noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);
                        screenText.setTextLineNumber(screenText.getTextLineNumber() + 1);
                        noteFrame.repaint();
                    }
                }
                screenText.deselect();
            }


        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {//left arrow key
            if (keyPressed.size() == 2 && keyPressed.contains(16)) {//shift + left

                if (noteFrame.getBlinker().index() == 0) {//vaghti avale khate
                    if (screenText.getTextLineNumber() != 0) {//vaghti ghblesh khati vujud dare
                        screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);
                        noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                        noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() - 20);
                    }
                } else {
                    screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(noteFrame.getBlinker().index() - 1).setSelected(!screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(noteFrame.getBlinker().index() - 1).isSelected());
                    noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() - 7);
                    noteFrame.select(noteFrame.getGraphics());

                }
                noteFrame.repaint();


            } else if (keyPressed.size() == 2 && keyPressed.contains(17)) {//ctrl + left
                if (noteFrame.getBlinker().index() == 0) {
                    if (screenText.getTextLineNumber() != 0) {
                        screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);
                        noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() - 20);
                        noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                    }

                } else {
                    if (screenText.findPreviousSpace() == -1) {
                        if (screenText.getTextLineNumber() != 0) {
                            screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);
                            noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() - 20);
                            noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                        } else {
                            noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                        }
                    } else {
                        noteFrame.getBlinker().setX(screenText.findPreviousSpace() * 7 + noteFrame.LINE_BEGINNER);
                    }
                }
                screenText.deselect();
                noteFrame.repaint();
            } else if (keyPressed.size() == 3 && keyPressed.contains(16) && keyPressed.contains(17)) {
                if (noteFrame.getBlinker().index() == 0) {
                    if (screenText.getTextLineNumber() != 0) {
                        screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);
                        noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() - 20);
                        noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                    }

                } else {
                    if (screenText.findPreviousSpace() == -1) {
                        if (screenText.getTextLineNumber() != 0) {
                            for (int i = noteFrame.getBlinker().index() - 1; i >= 0; i--) {
                                boolean selected = screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).isSelected();
                                screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(!selected);
                            }
                            screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);
                            noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() - 20);
                            noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                        } else {
                            for (int i = noteFrame.getBlinker().index() - 1; i >= 0; i--) {
                                boolean selected = screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).isSelected();
                                screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(!selected);
                            }
                            noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                        }
                    } else {
                        int index = noteFrame.getBlinker().index();
                        noteFrame.getBlinker().setX(screenText.findPreviousSpace() * 7 + noteFrame.LINE_BEGINNER);
                        for (int i = index - 1; i >= noteFrame.getBlinker().index(); i--) {
                            boolean selected = screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).isSelected();
                            screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(!selected);
                        }
                    }
                }
                noteFrame.repaint();
            } else {//left
                if (noteFrame.getBlinker().getX() - 7 >= noteFrame.LINE_BEGINNER) {
                    if (screenText.isSelectAll()) screenText.setSelectAll(false);
                    noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() - 7);// 7 = length of each character in monospaced mode
                    noteFrame.repaint();
                } else if (noteFrame.getBlinker().index() == 0) {
                    if (screenText.getTextLineNumber() != 0) {
                        screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);
                        noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() - 20);
                        noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                        noteFrame.repaint();
                    }
                }
                screenText.deselect();
            }


        } else if (e.getKeyCode() == KeyEvent.VK_UP) {//up arrow key
            if (keyPressed.size() == 2 && keyPressed.contains(16)) {//shift + up
                if (screenText.getTextLineNumber() > 0) {
                    int index = noteFrame.getBlinker().index();
                    for (int i = index - 1; i >= 0; i--) {
                        screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(!screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).isSelected());
                    }
                    screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);
                    for (int i = index; i < screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size(); i++) {
                        screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(!screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).isSelected());
                    }
                    noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() - 20);
                    if (noteFrame.getBlinker().getX() > noteFrame.LINE_BEGINNER + screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7) {
                        noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER + screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7);
                    }
                    noteFrame.repaint();
                }


            } else {//up
                if (screenText.getTextLineNumber() - 1 >= 0) {
                    if (screenText.isSelectAll()) screenText.setSelectAll(false);
                    noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() - 20);// each line seperate by 20 pixels
                    if (noteFrame.getBlinker().getX() > screenText.getTextLines().get(screenText.getTextLineNumber() - 1).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER) {
                        noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber() - 1).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                    }
                    screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);
                    noteFrame.repaint();
                }
                screenText.deselect();
            }

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {//down arrow key
            if (keyPressed.size() == 2 && keyPressed.contains(16)) {//shift + down
                if (screenText.getTextLineNumber() < screenText.getTextLines().size() - 1) {
                    int index = noteFrame.getBlinker().index();
                    for (int i = index; i < screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size(); i++) {
                        screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(!screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).isSelected());
                    }
                    screenText.setTextLineNumber(screenText.getTextLineNumber() + 1);
                    index = Math.min(index, screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size());
                    for (int i = 0; i < index; i++) {
                        screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(!screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).isSelected());
                    }
                    if (noteFrame.getBlinker().getX() > noteFrame.LINE_BEGINNER + screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7) {
                        noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER + screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7);
                    }
                    noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);
                    noteFrame.repaint();
                }
            } else {//down
                if (screenText.getTextLineNumber() + 1 < screenText.getTextLines().size()) {
                    if (screenText.isSelectAll()) screenText.setSelectAll(false);
                    noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);//each line seperate by 20 pixels
                    if (noteFrame.getBlinker().getX() > screenText.getTextLines().get(screenText.getTextLineNumber() + 1).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER) {
                        noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber() + 1).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                    }
                    screenText.setTextLineNumber(screenText.getTextLineNumber() + 1);
                    noteFrame.repaint();
                }
                screenText.deselect();
            }

        } else if (e.getKeyCode() == KeyEvent.VK_HOME) {//HOme
            if (screenText.isSelectAll()) screenText.setSelectAll(false);
            if (keyPressed.size() == 2 && keyPressed.contains(17)) {//ctrl + home
                noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                noteFrame.getBlinker().setY(50);
                screenText.setTextLineNumber(0);
            } else if (keyPressed.size() == 2 && keyPressed.contains(16)) { // shift + home
                if (noteFrame.getBlinker().index() != 0) {
                    for (int i = noteFrame.getBlinker().index() - 1; i >= 0; i--) {
                        screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(true);

                    }
                }
                noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                noteFrame.repaint();
            } else if (keyPressed.size() == 3 && keyPressed.contains(16) && keyPressed.contains(17)) { // shift + ctrl + home
                for (int i = screenText.getTextLineNumber(); i >= 0; i--) {
                    if (i == screenText.getTextLineNumber()) {
                        for (int j = 0; j < noteFrame.getBlinker().index(); j++) {
                            screenText.getTextLines().get(i).getStyledCharacters().get(j).setSelected(true);
                        }
                    } else {
                        for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                            screenText.getTextLines().get(i).getStyledCharacters().get(j).setSelected(true);
                        }
                    }
                }
                noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                noteFrame.getBlinker().setY(50);
                screenText.setTextLineNumber(0);
                noteFrame.repaint();
            } else {//home
                noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                screenText.deselect();
            }

            noteFrame.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_END) {//END
            if (screenText.isSelectAll()) screenText.setSelectAll(false);
            if (keyPressed.size() == 2 && keyPressed.contains(17)) {//ctrl + end
                //17=ctrl
                screenText.setTextLineNumber(screenText.getTextLines().size() - 1);
                noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER + screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7);
                noteFrame.getBlinker().setY(50 + 20 * (screenText.getTextLineNumber()));
            } else if (keyPressed.size() == 2 && keyPressed.contains(16)) {//shift + end

                for (int i = noteFrame.getBlinker().index(); i < screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size(); i++) {
                    screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).setSelected(true);
                }
                int i = screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size();
                noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER + i * 7);
                noteFrame.repaint();

            } else if (keyPressed.size() == 3 && keyPressed.contains(16) && keyPressed.contains(17)) {// ctrl + shift + end
                for (int i = screenText.getTextLineNumber(); i < screenText.getTextLines().size(); i++) {
                    if (i == screenText.getTextLineNumber()) {
                        for (int j = noteFrame.getBlinker().index(); j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                            screenText.getTextLines().get(i).getStyledCharacters().get(j).setSelected(true);
                        }
                    } else {
                        for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                            screenText.getTextLines().get(i).getStyledCharacters().get(j).setSelected(true);
                        }
                    }
                }
                screenText.setTextLineNumber(screenText.getTextLines().size() - 1);
                noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                noteFrame.getBlinker().setY(50 + screenText.getTextLineNumber() * 20);
                noteFrame.repaint();

            } else {//end
                noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                screenText.deselect();
            }
            noteFrame.repaint();

        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {//DELETE
            if (screenText.isSelectAll()) {
                screenText.getTextLines().clear();
                screenText.getTextLines().add(0, new TextLine());
                screenText.setSelectAll(false);
                screenText.setTextLineNumber(0);
                noteFrame.repaint();
                noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                noteFrame.getBlinker().setY(50);
            } else if (screenText.thereIsSelected()) {
                noteFrame.removeSelected();
                noteFrame.repaint();
            } else {//delete
                if (noteFrame.getBlinker().index() < screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size()) {
                    noteFrame.delete();
                } else {
                    if (screenText.getTextLineNumber() != screenText.getTextLines().size() - 1) {
                        for (int i = 0; i < screenText.getTextLines().get(screenText.getTextLineNumber() + 1).getStyledCharacters().size(); i++) {
                            screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().add(new StyledCharacter(screenText.getTextLines().get(screenText.getTextLineNumber() + 1).getStyledCharacters().get(i).toChar()));
                            screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() - 1).setHasValue(true);

                        }
                        screenText.getTextLines().remove(screenText.getTextLineNumber() + 1);
                        noteFrame.repaint();
                    }
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {//tab
            for (int i = 0; i < 6; i++) {
                screenText.addCharacter(new StyledCharacter(' '), noteFrame.getBlinker().index());
            }
            noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() + 42);
            noteFrame.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_C && keyPressed.size() == 2 && keyPressed.contains(17)) {//copy ctrl + c
            screenText.copy();


        } else if (e.getKeyCode() == KeyEvent.VK_V && keyPressed.size() == 2 && keyPressed.contains(17)) {//paste ctrl + v
            if (screenText.thereIsSelected()) noteFrame.removeSelected();
            screenText.paste(noteFrame.getBlinker().index());
            screenText.deselect();
            noteFrame.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_X && keyPressed.size() == 2 && keyPressed.contains(17)) {//cut ctrl + x
            if (screenText.thereIsSelected())
                screenText.cut();
        } else if (e.getKeyCode() == KeyEvent.VK_L && keyPressed.size() == 2 && keyPressed.contains(17)) {// ctrl + l switch between show line number mode

            noteFrame.setShowLines(!noteFrame.isShowLines());
            noteFrame.lineNumbers();
            noteFrame.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_W && keyPressed.size() == 2 && keyPressed.contains(17)) {//ctrl + w switch between word wrap mode

            if (screenText.isWordWrapMode()) {
                screenText.setWordWrapMode(false);
                for (int i = 0; i < screenText.getTextLines().size(); i++) {
                    if (screenText.getTextLines().get(i).getStyledCharacters().size() != 0) {
                        if (screenText.getTextLines().get(i).getStyledCharacters().get(screenText.getTextLines().get(i).getStyledCharacters().size() - 1).toChar() == '\n') {
                            screenText.getTextLines().get(i).getStyledCharacters().remove(screenText.getTextLines().get(i).getStyledCharacters().size() - 1);
                            ArrayList<StyledCharacter> temp = new ArrayList<>();
                            if (i != screenText.getTextLines().size() - 1) {
                                for (int j = 0; j < screenText.getTextLines().get(i + 1).getStyledCharacters().size(); j++) {
                                    temp.add(new StyledCharacter(screenText.getTextLines().get(i + 1).getStyledCharacters().get(j).toChar()));
                                }
                                for (int j = 0; j < temp.size(); j++) {
                                    screenText.getTextLines().get(i + 1).getStyledCharacters().remove(0);
                                }
                                screenText.getTextLines().remove(i + 1);
                                screenText.setTextLineNumber(screenText.getTextLineNumber() - 1);
                                for (int j = 0; j < temp.size(); j++) {
                                    screenText.addCharacter(new StyledCharacter(temp.get(j).toChar()), screenText.getTextLines().get(i).getStyledCharacters().size());
                                }
                                noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() - 20);
                                noteFrame.getBlinker().setX(screenText.getTextLines().get(i).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                            }
                            temp.clear();
                        }
                    }

                }
                noteFrame.repaint();


            } else {//word wrap mode was off
                screenText.setWordWrapMode(true);


                for (int i = 0; i < screenText.getTextLines().size(); i++) {
                    if (screenText.getTextLines().get(i).getStyledCharacters().size() > 100) {
                        ArrayList<StyledCharacter> temp = new ArrayList<>();
                        if (screenText.getTextLines().get(i).findPreviousSpace(screenText.getTextLines().get(i).getStyledCharacters().size()) == 0) {//space nadashte bashe
                            for (int j = 100; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                                temp.add(new StyledCharacter(screenText.getTextLines().get(i).getStyledCharacters().get(j).toChar()));
                            }
                            for (int j = 0; j < temp.size(); j++) {
                                screenText.getTextLines().get(i).getStyledCharacters().remove(100);
                            }
                            screenText.getTextLines().get(i).getStyledCharacters().add(100, new StyledCharacter('\n'));
                            screenText.getTextLines().get(i).getStyledCharacters().get(100).setHasValue(false);
                            noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                            noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);
                            screenText.setTextLineNumber(screenText.getTextLineNumber() + 1);

                        } else {//space dashte bashe
                            int space = screenText.getTextLines().get(i).findPreviousSpace(screenText.getTextLines().get(i).getStyledCharacters().size());
                            for (int j = space; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                                temp.add(new StyledCharacter(screenText.getTextLines().get(i).getStyledCharacters().get(j).toChar()));
                            }
                            for (int j = 0; j < temp.size(); j++) {
                                screenText.getTextLines().get(i).getStyledCharacters().remove(space);
                            }
                            screenText.getTextLines().get(i).getStyledCharacters().add(space, new StyledCharacter('\n'));
                            noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                            noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);
                            screenText.setTextLineNumber(screenText.getTextLineNumber() + 1);
                        }
                        screenText.getTextLines().add(i + 1, new TextLine());
                        for (int j = 0; j < temp.size(); j++) {
                            screenText.getTextLines().get(i + 1).getStyledCharacters().add(new StyledCharacter(temp.get(j).toChar()));
                            screenText.getTextLines().get(i + 1).getStyledCharacters().get(j).setHasValue(true);
                        }
                    }
                }


            }

            noteFrame.repaint();
            keyPressed.clear();

        } else if (e.getKeyCode() == KeyEvent.VK_S && keyPressed.size() == 2 && keyPressed.contains(17))

        {//ctrl + s save
            if (noteFrame.isSaved()) {//az ghabl save shode bud
                BufferedWriter bw = null;
                try {
                    String path = noteFrame.getPath();


                    File file = new File(path);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fw = new FileWriter(file);
                    bw = new BufferedWriter(fw);
                    for (int i = 0; i < screenText.getTextLines().size(); i++) {
                        String temp = screenText.getTextLines().get(i).toString();
                        bw.write(temp);
                        if (i != screenText.getTextLines().size() - 1)
                            bw.newLine();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    if (bw != null) {
                        try {
                            bw.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                }
            } else {//save nashode bud
                JFrame f = new JFrame();
                JFileChooser save = new JFileChooser();
                int option = save.showSaveDialog(f);
                int fileToSave = option;
                f.add(save);
                f.setVisible(true);
                f.pack();
                if (fileToSave == JFileChooser.APPROVE_OPTION) {
                    BufferedWriter bw = null;
                    try {
                        String path = save.getSelectedFile().getPath();
                        noteFrame.setPath(path);
                        System.out.println(path);
                        File file = new File(path);

                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileWriter fw = new FileWriter(file);
                        bw = new BufferedWriter(fw);
                        for (int i = 0; i < screenText.getTextLines().size(); i++) {
                            String temp = screenText.getTextLines().get(i).toString();
                            bw.write(temp);
                            if (i != screenText.getTextLines().size() - 1)
                                bw.newLine();
                        }
                        System.out.println("File Written Successfully");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        if (bw != null) {
                            try {
                                bw.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        f.setVisible(false);
                        f.dispose();
                        noteFrame.setSaved(true);
                    }
                } else if (fileToSave == JFileChooser.CANCEL_OPTION) {
                    f.setVisible(false);
                    f.dispose();
                }

            }

            keyPressed.clear();


        } else if (e.getKeyCode() == KeyEvent.VK_O && keyPressed.size() == 2 && keyPressed.contains(17))

        {//ctrl + o open

            if (noteFrame.isSaved()) {//file e feli save shode bud
                try {
                    if (!eq(new File(noteFrame.getPath()), noteFrame.getScreenText())) {//taghir karde
                        JFrame f = new JFrame();
                        f.setLayout(new FlowLayout());
                        JLabel label = new JLabel("Do you want to save changes ? ");
                        JButton yes = new JButton("YES");
                        JButton no = new JButton("NO");
                        f.add(label);
                        f.add(yes);
                        f.add(no);
                        f.setVisible(true);
                        f.pack();
                        yes.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                String path = noteFrame.getPath();
                                File file = new File(path);
                                try {
                                    FileWriter fr = new FileWriter(file);
                                    BufferedWriter bw = new BufferedWriter(fr);
                                    for (int i = 0; i < screenText.getTextLines().size(); i++) {
                                        String temp = screenText.getTextLines().get(i).toString();
                                        try {
                                            bw.write(temp);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        if (i != screenText.getTextLines().size() - 1)
                                            try {
                                                bw.newLine();
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }

                                    }
                                    bw.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                f.setVisible(false);
                                f.dispose();
                                JFileChooser open = new JFileChooser();
                                int option = open.showOpenDialog(noteFrame);
                                int fileToOpen = option;
                                JFrame f = new JFrame();
                                f.setLayout(new FlowLayout());
                                f.add(open);
                                f.setVisible(true);
                                f.pack();
                                if (fileToOpen == JFileChooser.APPROVE_OPTION) {
                                    ArrayList<String> lines = new ArrayList<>();
                                    path = open.getSelectedFile().getPath();
                                    file = new File(path);
                                    FileReader fr = null;
                                    BufferedReader br = null;
                                    try {
                                        fr = new FileReader(file);
                                        br = new BufferedReader(fr);
                                        String line;
                                        while ((line = br.readLine()) != null) {
                                            lines.add(line);
                                        }
                                    } catch (FileNotFoundException e1) {
                                        e1.printStackTrace();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    screenText.getTextLines().clear();

                                    for (int i = 0; i < lines.size(); i++) {
                                        screenText.getTextLines().add(i, new TextLine());
                                        for (int j = 0; j < lines.get(i).length(); j++) {
                                            screenText.getTextLines().get(i).getStyledCharacters().add(new StyledCharacter(lines.get(i).charAt(j)));
                                            screenText.getTextLines().get(i).getStyledCharacters().get(j).setHasValue(true);
                                        }
                                    }
                                    noteFrame.setPath(path);
                                    noteFrame.setSaved(true);
                                    screenText.setTextLineNumber(screenText.getTextLines().size() - 1);
                                    noteFrame.getBlinker().setY(50 + screenText.getTextLineNumber() * 20);
                                    noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                                    noteFrame.repaint();
                                    f.dispose();
                                    f.setVisible(false);

                                } else if (fileToOpen == JFileChooser.CANCEL_OPTION) {
                                    f.setVisible(false);
                                    f.dispose();
                                }


                            }
                        });
                        no.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                f.setVisible(false);
                                f.dispose();
                                try {
                                    noteFrame.setVisible(false);
                                    NoteFrame newFrame = new NoteFrame();
                                    noteFrame = newFrame;
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                noteFrame.repaint();
                            }
                        });


                    } else {//taghir nakarde
                        JFileChooser open = new JFileChooser();
                        int option = open.showOpenDialog(noteFrame);
                        int fileToOpen = option;
                        JFrame f = new JFrame();
                        f.setLayout(new FlowLayout());
                        f.add(open);
                        f.setVisible(true);
                        f.pack();
                        if (fileToOpen == JFileChooser.APPROVE_OPTION) {
                            ArrayList<String> lines = new ArrayList<>();
                            String path = open.getSelectedFile().getPath();
                            File file = new File(path);
                            FileReader fr = null;
                            BufferedReader br = null;
                            try {
                                fr = new FileReader(file);
                                br = new BufferedReader(fr);
                                String line;
                                while ((line = br.readLine()) != null) {
                                    lines.add(line);
                                }
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            screenText.getTextLines().clear();

                            for (int i = 0; i < lines.size(); i++) {
                                screenText.getTextLines().add(i, new TextLine());
                                for (int j = 0; j < lines.get(i).length(); j++) {
                                    screenText.getTextLines().get(i).getStyledCharacters().add(new StyledCharacter(lines.get(i).charAt(j)));
                                    screenText.getTextLines().get(i).getStyledCharacters().get(j).setHasValue(true);
                                }
                            }
                            noteFrame.setPath(path);
                            noteFrame.setSaved(true);
                            screenText.setTextLineNumber(screenText.getTextLines().size() - 1);
                            noteFrame.getBlinker().setY(50 + screenText.getTextLineNumber() * 20);
                            noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                            noteFrame.repaint();
                            f.dispose();
                            f.setVisible(false);

                        } else if (fileToOpen == JFileChooser.CANCEL_OPTION) {
                            f.setVisible(false);
                            f.dispose();
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {//save nashode bud
                JFileChooser open = new JFileChooser();
                int option = open.showOpenDialog(noteFrame);
                int fileToOpen = option;
                JFrame f = new JFrame();
                f.setLayout(new FlowLayout());
                f.add(open);
                f.setVisible(true);
                f.pack();
                if (fileToOpen == JFileChooser.APPROVE_OPTION) {
                    ArrayList<String> lines = new ArrayList<>();
                    String path = open.getSelectedFile().getPath();
                    File file = new File(path);
                    FileReader fr = null;
                    BufferedReader br = null;
                    try {
                        fr = new FileReader(file);
                        br = new BufferedReader(fr);
                        String line;
                        while ((line = br.readLine()) != null) {
                            lines.add(line);
                        }
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    screenText.getTextLines().clear();

                    for (int i = 0; i < lines.size(); i++) {
                        screenText.getTextLines().add(i, new TextLine());
                        for (int j = 0; j < lines.get(i).length(); j++) {
                            screenText.getTextLines().get(i).getStyledCharacters().add(new StyledCharacter(lines.get(i).charAt(j)));
                            screenText.getTextLines().get(i).getStyledCharacters().get(j).setHasValue(true);
                        }
                    }
                    noteFrame.setPath(path);
                    noteFrame.setSaved(true);
                    screenText.setTextLineNumber(screenText.getTextLines().size() - 1);
                    noteFrame.getBlinker().setY(50 + screenText.getTextLineNumber() * 20);
                    noteFrame.getBlinker().setX(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().size() * 7 + noteFrame.LINE_BEGINNER);
                    noteFrame.repaint();
                    f.dispose();
                    f.setVisible(false);

                } else if (fileToOpen == JFileChooser.CANCEL_OPTION) {
                    f.setVisible(false);
                    f.dispose();
                }
            }
            keyPressed.clear();
        } else if (e.getKeyCode() == KeyEvent.VK_Z && keyPressed.size() == 2 && keyPressed.contains(17))

        {//Undo ctrl + z

            screenText.getTextLines().clear();
            for (int i = 0; i < noteFrame.getBackup().getTextLines().size(); i++) {
                screenText.getTextLines().add(i, new TextLine());
                for (int j = 0; j < noteFrame.getBackup().getTextLines().get(i).getStyledCharacters().size(); j++) {
                    screenText.getTextLines().get(i).getStyledCharacters().add(new StyledCharacter(noteFrame.getBackup().getTextLines().get(i).getStyledCharacters().get(j).toChar()));
                    screenText.getTextLines().get(i).getStyledCharacters().get(j).setHasValue(true);
                    if (noteFrame.getBackup().getTextLines().get(i).getStyledCharacters().get(j).isSelected()) {
                        screenText.getTextLines().get(i).getStyledCharacters().get(j).setSelected(true);
                    }
                }
            }
            noteFrame.repaint();

        } else if (e.getKeyCode() == KeyEvent.VK_Y && keyPressed.size() == 2 && keyPressed.contains(17))

        {//ctrl + y redo


            int j = noteFrame.getBlinker().index();
            for (int i = screenText.findPreviousSpace(); i < j; i++) {
                screenText.addCharacter(new StyledCharacter(screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(i).toChar()), noteFrame.getBlinker().index());
                noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() + 7);
            }
            noteFrame.repaint();
            keyPressed.clear();
        } else if (e.getKeyCode() == KeyEvent.VK_F && keyPressed.size() == 2 && keyPressed.contains(17))

        {//ctrl + f find
            JFrame f = new JFrame();
            f.setVisible(true);
            f.setLayout(new FlowLayout());
            JLabel label = new JLabel("Find What");
            TextField textField = new TextField(10);
            JButton findButton = new JButton("Find");
            f.add(label);
            f.add(textField);
            f.add(findButton);
            f.pack();

            findButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String word = textField.getText().toLowerCase();
                    fWord = word;
                    screenText.deselect();
                    a:
                    for (int i = 0; i < screenText.getTextLines().size(); i++) {

                        for (int j = 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                            try {
                                String temp = screenText.getTextLines().get(i).toString().substring(j, j + fWord.length());

                                if (temp.toLowerCase().equals(fWord.toLowerCase())) {
                                    for (int k = 0; k < fWord.length(); k++) {
                                        screenText.getTextLines().get(i).getStyledCharacters().get(j + k).setSelected(true);
                                    }
                                    screenText.setTextLineNumber(i);
                                    noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER + j * 7);
                                    noteFrame.getBlinker().setY(50 + (i) * 20);
                                    break a;

                                }
                            } catch (Exception ex) {
                                break;
                            }
                        }
                    }

                    f.dispose();
                    noteFrame.repaint();
                }
            });
            keyPressed.clear();

            this.replace = false;
        } else if (e.getKeyCode() == KeyEvent.VK_G && keyPressed.size() == 2 && keyPressed.contains(17))

        {//find next
            int line = 0;
            int index = 0;
            if (screenText.thereIsSelected()) {
                a:
                for (int i = 0; i < screenText.getTextLines().size(); i++) {
                    String temp = screenText.getTextLines().get(i).toString();
                    for (int j = 0; j < temp.length(); j++) {
                        if (screenText.getTextLines().get(i).getStyledCharacters().get(j).isSelected()) {
                            line = i;
                            index = j;
                            break a;
                        }
                    }
                }
            } else {
                line = ((noteFrame.getBlinker().getY() - 50) / 20);
                index = noteFrame.getBlinker().index();
            }

            screenText.deselect();

            b:
            for (int i = line; i < screenText.getTextLines().size(); i++) {

                for (int j = i == line ? index + 1 : 0; j < screenText.getTextLines().get(i).getStyledCharacters().size(); j++) {
                    try {
                        String temp = screenText.getTextLines().get(i).toString().substring(j, j + fWord.length());

                        if (temp.toLowerCase().equals(fWord.toLowerCase())) {
                            for (int k = 0; k < fWord.length(); k++) {
                                screenText.getTextLines().get(i).getStyledCharacters().get(j + k).setSelected(true);
                            }
                            screenText.setTextLineNumber(i);
                            noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER + j * 7);
                            noteFrame.getBlinker().setY(50 + (i) * 20);
                            break b;

                        }
                    } catch (Exception ex) {
                        break;
                    }
                }
            }
            noteFrame.repaint();


        } else if (e.getKeyCode() == KeyEvent.VK_R && keyPressed.size() == 2 && keyPressed.contains(17))

        {// ctrl + r replace text

            if (screenText.thereIsSelected()) {
                if (!this.replace) {
                    JFrame f = new JFrame();
                    f.setLayout(new FlowLayout());
                    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    JLabel label = new JLabel("Replace With :");
                    TextField textField = new TextField();
                    JButton replace = new JButton("Replace");
                    f.add(label);
                    f.add(textField);
                    f.add(replace);
                    f.pack();
                    f.setVisible(true);
                    this.replace = true;
                    replace.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            replaceWith = textField.getText();


                            f.dispose();
                            f.setVisible(false);
                            noteFrame.removeSelected();
                            for (int i = 0; i < replaceWith.length(); i++) {

                                screenText.addCharacter(new StyledCharacter(replaceWith.charAt(i)), noteFrame.getBlinker().index());
                                noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() + 7);
                            }
                        }
                    });


                } else {
                    noteFrame.removeSelected();

                    for (int i = 0; i < replaceWith.length(); i++) {

                        screenText.addCharacter(new StyledCharacter(replaceWith.charAt(i)), noteFrame.getBlinker().index());
                        noteFrame.getBlinker().setX(noteFrame.getBlinker().getX() + 7);
                    }

                }


            } else {
                this.replace = false;
                replaceWith = "";
            }
            keyPressed.clear();
        } else if (e.getKeyCode() == KeyEvent.VK_N && keyPressed.size() == 2 && keyPressed.contains(17))

        {//ctrl + n newFile


            if (noteFrame.isSaved()) {//age save shode
                try {
                    if (eq(new File(noteFrame.getPath()), screenText)) {//taghiri nakarde
                        screenText.getTextLines().clear();
                        screenText.getTextLines().add(0, new TextLine());
                        noteFrame.setPath("");
                        noteFrame.setSaved(false);
                        noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                        noteFrame.getBlinker().setY(50);
                        screenText.setTextLineNumber(0);
                        noteFrame.repaint();
                    } else {//taghir karde
                        JFrame f = new JFrame();
                        f.setLayout(new FlowLayout());
                        JLabel label = new JLabel("Do you want to save changes ? ");
                        JButton yes = new JButton("YES");
                        JButton no = new JButton("NO");
                        f.add(label);
                        f.add(yes);
                        f.add(no);
                        f.setVisible(true);
                        f.pack();
                        yes.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                String path = noteFrame.getPath();
                                File file = new File(path);
                                try {
                                    FileWriter fr = new FileWriter(file);
                                    BufferedWriter bw = new BufferedWriter(fr);
                                    for (int i = 0; i < screenText.getTextLines().size(); i++) {
                                        String temp = screenText.getTextLines().get(i).toString();
                                        try {
                                            bw.write(temp);
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        if (i != screenText.getTextLines().size() - 1)
                                            try {
                                                bw.newLine();
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }

                                    }
                                    bw.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }

                                f.setVisible(false);
                                f.dispose();
                                try {
                                    noteFrame.setVisible(false);
                                    NoteFrame newFrame = new NoteFrame();
                                    noteFrame = newFrame;
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                noteFrame.repaint();


                            }
                        });
                        no.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                f.setVisible(false);
                                f.dispose();
                                try {
                                    noteFrame.setVisible(false);
                                    NoteFrame newFrame = new NoteFrame();
                                    noteFrame = newFrame;
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                noteFrame.repaint();
                            }
                        });
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {//age save nashode
                JFrame f = new JFrame();
                f.setLayout(new FlowLayout());
                JLabel label = new JLabel("Do you want to save changes ? ");
                JButton yes = new JButton("YES");
                JButton no = new JButton("NO");
                f.add(label);
                f.add(yes);
                f.add(no);
                f.setVisible(true);
                f.pack();
                yes.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame f2 = new JFrame();
                        JFileChooser save = new JFileChooser();
                        int option = save.showSaveDialog(f2);
                        int fileToSave = option;
                        f2.add(save);
                        f2.setVisible(true);
                        f2.pack();
                        if (fileToSave == JFileChooser.APPROVE_OPTION) {
                            BufferedWriter bw = null;
                            try {
                                String path = save.getSelectedFile().getPath();
                                noteFrame.setPath(path);
                                System.out.println(path);
                                File file = new File(path);

                                if (!file.exists()) {
                                    file.createNewFile();
                                }
                                FileWriter fw = new FileWriter(file);
                                bw = new BufferedWriter(fw);
                                for (int i = 0; i < screenText.getTextLines().size(); i++) {
                                    String temp = screenText.getTextLines().get(i).toString();
                                    bw.write(temp);
                                    if (i != screenText.getTextLines().size() - 1)
                                        bw.newLine();
                                }
                                System.out.println("File Written Successfully");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } finally {
                                if (bw != null) {
                                    try {
                                        bw.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                f2.setVisible(false);
                                f2.dispose();
                                f.setVisible(false);
                                f.dispose();
                                try {
                                    noteFrame.setVisible(false);
                                    NoteFrame newFrame = new NoteFrame();
                                    noteFrame = newFrame;
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                noteFrame.repaint();

                            }
                        } else if (fileToSave == JFileChooser.CANCEL_OPTION) {
                            f2.setVisible(false);
                            f2.dispose();
                            f.setVisible(false);
                            f.dispose();
                            try {
                                noteFrame.setVisible(false);
                                NoteFrame newFrame = new NoteFrame();
                                noteFrame = newFrame;
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
                no.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            noteFrame.setVisible(false);
                            NoteFrame newFrame = new NoteFrame();
                            noteFrame = newFrame;
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                });


            }

        } else if (e.getKeyCode() == KeyEvent.VK_B && keyPressed.size() == 2 && keyPressed.contains(17))

        {//ctrl + b Save File As New One

            JFrame f = new JFrame();
            JFileChooser save = new JFileChooser();
            int option = save.showSaveDialog(f);
            int fileToSave = option;
            f.add(save);
            f.setVisible(true);
            f.pack();
            if (fileToSave == JFileChooser.APPROVE_OPTION) {
                BufferedWriter bw = null;
                try {
                    String path = save.getSelectedFile().getPath();
                    noteFrame.setPath(path);
                    System.out.println(path);
                    File file = new File(path);

                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fw = new FileWriter(file);
                    bw = new BufferedWriter(fw);
                    for (int i = 0; i < screenText.getTextLines().size(); i++) {
                        String temp = screenText.getTextLines().get(i).toString();
                        bw.write(temp);
                        if (i != screenText.getTextLines().size() - 1)
                            bw.newLine();
                    }
                    System.out.println("File Written Successfully");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    if (bw != null) {
                        try {
                            bw.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    f.setVisible(false);
                    f.dispose();
                    noteFrame.setSaved(true);
                }
            } else if (fileToSave == JFileChooser.CANCEL_OPTION) {
                f.setVisible(false);
                f.dispose();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_K && keyPressed.size() == 2 && keyPressed.contains(17))

        {//goto line number
            final int[] line = {0};
            JFrame f = new JFrame();
            f.setTitle("GOTO");
            f.setBounds(400, 300, 200, 100);
            f.setVisible(true);
            f.setLayout(new FlowLayout());
            TextField textArea = new TextField();
            textArea.setSize(50, 50);
            JButton button = new JButton("OK");
            JLabel label = new JLabel("Goto Line :");
            f.add(label);
            f.add(textArea);
            f.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    line[0] = Integer.parseInt(textArea.getText());
                    f.dispose();
                    int l = line[0] - 1;
                    if (l >= 0 && l < screenText.getTextLines().size()) {
                        screenText.setTextLineNumber(l);
                        noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                        noteFrame.getBlinker().setY(50 + l * 20);
                        noteFrame.repaint();
                    } else {
                        System.err.println("does not exist");
                    }
                }
            });
            keyPressed.clear();
        }  else if (e.getKeyCode() == KeyEvent.VK_ENTER)

        {// Enter !
            if (screenText.isSelectAll()) {
                noteFrame.removeSelected();
                int j = screenText.getTextLineNumber();
                screenText.setTextLineNumber(j + 1);
                screenText.getTextLines().add(screenText.getTextLineNumber(), new TextLine());
                noteFrame.getBlinker().setY(50 + screenText.getTextLineNumber() * 20);
                noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                screenText.setSelectAll(false);
                noteFrame.repaint();
            } else {
                if (noteFrame.getBlinker().index() < noteFrame.getBlinker().styledCharactersSize()) {
                    if (screenText.thereIsSelected()) {
                        noteFrame.removeSelected();
                    }
                    int i = noteFrame.getBlinker().getY();
                    noteFrame.getBlinker().setY(i + 20);
                    screenText.getTextLines().add(screenText.getTextLineNumber() + 1, new TextLine());
                    int size = noteFrame.getBlinker().styledCharactersSize();
                    for (int j = noteFrame.getBlinker().index(); j < size; j++) {
                        StyledCharacter temp = screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().get(j);
                        screenText.getTextLines().get(screenText.getTextLineNumber() + 1).getStyledCharacters().add(temp);
                    }
                    for (int j = noteFrame.getBlinker().index(); j < size; j++) {
                        screenText.getTextLines().get(screenText.getTextLineNumber()).getStyledCharacters().remove(noteFrame.getBlinker().index());
                    }
                    screenText.setTextLineNumber(screenText.getTextLineNumber() + 1);
                    noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                    noteFrame.repaint();
                } else {
                    if (screenText.thereIsSelected()) {
                        noteFrame.removeSelected();
                    }
                    int j = screenText.getTextLineNumber();
                    screenText.setTextLineNumber(j + 1);
                    screenText.getTextLines().add(screenText.getTextLineNumber(), new TextLine());
                    noteFrame.getBlinker().setY(50 + screenText.getTextLineNumber() * 20);
                    noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                    screenText.setSelectAll(false);
                    noteFrame.repaint();
                }
            }
            noteFrame.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed.remove(e.getKeyCode());


    }

    private boolean eq(File file, ScreenText screenText) throws IOException {// age neveshte haaye tooye file ba screentext yeki bashe true mide bara mavaqeE ke mikhaaym beporsim change ha save shan ya na
        ArrayList<String> scLines = new ArrayList<>();
        ArrayList<String> fileLines = new ArrayList<>();
        for (int i = 0; i < screenText.getTextLines().size(); i++) {
            scLines.add(screenText.getTextLines().get(i).toString());
        }
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        String line;
        while ((line = br.readLine()) != null) {
            fileLines.add(line);
        }
        if (fileLines.size() != scLines.size()) return false;
        for (int i = 0; i < fileLines.size(); i++) {
            if (!fileLines.get(i).equals(scLines.get(i))) return false;
        }
        return true;
    }

}
