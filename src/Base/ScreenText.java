package Base;


import Appearance.NoteFrame;

import java.util.ArrayList;

public class ScreenText {

    private ArrayList<TextLine> textLines = new ArrayList<>();
    private boolean selectAll = false;
    private int textLineNumber = 0;
    private static ArrayList<TextLine> cutCopy = new ArrayList<>();
    NoteFrame noteFrame;
    private boolean wordWrapMode = false;
    private ArrayList<TextLine> undo = new ArrayList<>();
    private int lineWordWrap = -1;


    {
        for (int i = 0; i < 1; i++) {
            textLines.add(i, new TextLine());
        }
    }

    public ScreenText() {
    }

    public ScreenText(NoteFrame noteFrame) {
        this.noteFrame = noteFrame;
    }

    public ArrayList<TextLine> getUndo() {
        return undo;
    }

    public ArrayList<TextLine> getTextLines() {
        return textLines;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public int getTextLineNumber() {
        return textLineNumber;
    }

    public void setTextLineNumber(int textLineNumber) {
        this.textLineNumber = textLineNumber;
    }

    public void removeLine() {
        if (textLineNumber != 0) {
            textLines.remove(textLineNumber);
//            textLines.add(textLineNumber,new TextLine());
//            textLines.get(textLineNumber).getStyledCharacters().add(new StyledCharacter(' '));
            setTextLineNumber(textLineNumber - 1);
        } else {
            textLines.remove(textLineNumber);
            if (textLines.size() == 0) {
                textLines.add(new TextLine());
            }

        }


    }

    public void addCharacter(StyledCharacter temp, int index) {
        if (wordWrapMode && textLines.get(textLineNumber).getStyledCharacters().size() == 100) {
            if (noteFrame.getBlinker().index() == textLines.get(textLineNumber).getStyledCharacters().size()) {//age dare be tahe khat ezafe mishe
                if (textLines.get(textLineNumber).findPreviousSpace(textLines.get(textLineNumber).getStyledCharacters().size()) == 0) {//there is no space in line
                    textLines.add(textLineNumber + 1, new TextLine());

                    if (textLines.get(textLineNumber).getStyledCharacters().size()<101){
                        textLines.get(textLineNumber).getStyledCharacters().add(100, new StyledCharacter('\n'));
                        textLines.get(textLineNumber).getStyledCharacters().get(100).setHasValue(false);
                    }
                    textLineNumber++;
                    index = 0;
                    noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER);
                    noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);

                } else {

                    int space = textLines.get(textLineNumber).findPreviousSpace(textLines.get(textLineNumber).getStyledCharacters().size());
                    ArrayList<StyledCharacter> characters = new ArrayList<>();
                    for (int i = space; i < textLines.get(textLineNumber).getStyledCharacters().size(); i++) {
                        characters.add(new StyledCharacter(textLines.get(textLineNumber).getStyledCharacters().get(i).toChar()));
                    }
                    for (int i = 0; i < characters.size(); i++) {
                        textLines.get(textLineNumber).getStyledCharacters().remove(space);
                    }
                    textLines.add(textLineNumber + 1, new TextLine());
                    textLines.get(textLineNumber).getStyledCharacters().add(textLines.get(textLineNumber).getStyledCharacters().size(), new StyledCharacter('\n'));
                    textLines.get(textLineNumber).getStyledCharacters().get(textLines.get(textLineNumber).getStyledCharacters().size() - 1).setHasValue(false);
                    textLineNumber++;
                    for (int i = 0; i < characters.size(); i++) {
                        textLines.get(textLineNumber).getStyledCharacters().add(new StyledCharacter(characters.get(i).toChar()));
                        textLines.get(textLineNumber).getStyledCharacters().get(i).setHasValue(true);
                    }
                    noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);
                    noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER + characters.size() * 7);
                    index = characters.size();
                }

            } else {//age be tahe khat ezafe nmishe
                /*
                1-age space dashte bashe
                {
                    dare ba akharin kalame ezafe mishe
                    ya be akharin kalame ezafe nmishe
                }
                2-age space nadashte bashe {bayad doone doone bendaaze khate bad}
                 */
                if (textLines.get(textLineNumber).findPreviousSpace(textLines.get(textLineNumber).getStyledCharacters().size()) == 0) {// age space nadashte bashe
                    if (lineWordWrap != textLineNumber) {
                        textLines.add(textLineNumber + 1, new TextLine());
                    }
                    textLines.get(textLineNumber + 1).getStyledCharacters().add(0, new StyledCharacter(textLines.get(textLineNumber).getStyledCharacters().get(99).toChar()));
                    textLines.get(textLineNumber + 1).getStyledCharacters().get(0).setHasValue(true);
                    textLines.get(textLineNumber).getStyledCharacters().remove(99);
                    lineWordWrap = textLineNumber;
//                    textLineNumber++;


                } else {//space dashte bashe
                    int space = textLines.get(textLineNumber).findPreviousSpace(textLines.get(textLineNumber).getStyledCharacters().size());
                    if (noteFrame.getBlinker().index() >= space) {//age tooye kalameye akhar bud
//                        System.out.println("rnu");
                        int ind = noteFrame.getBlinker().index() - space;
                        System.out.println(ind);
                        ArrayList<StyledCharacter> characters = new ArrayList<>();
                        for (int i = space; i < textLines.get(textLineNumber).getStyledCharacters().size(); i++) {
                            characters.add(new StyledCharacter(textLines.get(textLineNumber).getStyledCharacters().get(i).toChar()));
                        }
                        for (int i = 0; i < characters.size(); i++) {
                            textLines.get(textLineNumber).getStyledCharacters().remove(space);
                        }
                        textLines.add(textLineNumber + 1, new TextLine());
                        for (int i = 0; i < characters.size(); i++) {
                            textLines.get(textLineNumber + 1).getStyledCharacters().add(new StyledCharacter(characters.get(i).toChar()));
                            textLines.get(textLineNumber + 1).getStyledCharacters().get(i).setHasValue(true);
                        }
                        noteFrame.getBlinker().setX(noteFrame.LINE_BEGINNER + ind * 7);
                        noteFrame.getBlinker().setY(noteFrame.getBlinker().getY() + 20);
                        textLineNumber++;
                    } else {//age tooye kalamaye akhar nabud
                        ArrayList<StyledCharacter> characters = new ArrayList<>();
                        for (int i = space; i < textLines.get(textLineNumber).getStyledCharacters().size(); i++) {
                            characters.add(new StyledCharacter(textLines.get(textLineNumber).getStyledCharacters().get(i).toChar()));
                        }
                        for (int i = 0; i < characters.size(); i++) {
                            textLines.get(textLineNumber).getStyledCharacters().remove(space);
                        }
                        textLines.add(textLineNumber + 1, new TextLine());
                        for (int i = 0; i < characters.size(); i++) {
                            textLines.get(textLineNumber + 1).getStyledCharacters().add(new StyledCharacter(characters.get(i).toChar()));
                            textLines.get(textLineNumber + 1).getStyledCharacters().get(i).setHasValue(true);
                        }
                    }
                }


            }

        }
        textLines.get(textLineNumber).add(temp, index);
    }

    public boolean thereIsSelected() {
        for (int i = 0; i < textLines.size(); i++) {
            for (int j = 0; j < textLines.get(i).getStyledCharacters().size(); j++) {
                if (textLines.get(i).getStyledCharacters().get(j).isSelected()) return true;
            }
        }
        return false;
    }

    public void deselect() {
        for (int i = 0; i < textLines.size(); i++) {
            for (int j = 0; j < textLines.get(i).getStyledCharacters().size(); j++) {
                if (textLines.get(i).getStyledCharacters().get(j).isSelected())
                    textLines.get(i).getStyledCharacters().get(j).setSelected(false);
            }
        }
    }

    public void copy() {
        cutCopy.clear();
        ArrayList<TextLine> selected = new ArrayList<>();
        for (int i = 0; i < textLines.size(); i++) {
            TextLine temp = new TextLine();
            for (int j = 0; j < textLines.get(i).getStyledCharacters().size(); j++) {
                if (textLines.get(i).getStyledCharacters().get(j).isSelected())
                    temp.getStyledCharacters().add(new StyledCharacter(textLines.get(i).getStyledCharacters().get(j).toChar()));
            }
//            if (temp.getStyledCharacters().size() != 0) {
            selected.add(temp);
//            }
        }
        cutCopy = selected;


    }

    public void paste(int index) {
//        boolean first = false;
        ArrayList<StyledCharacter> characters = new ArrayList<>();
        if (textLines.size() == 0) {
            textLines.add(0, new TextLine());
//            textLines.get(0).add(new StyledCharacter(' '), 0);
//            first = true;
        }
        if (index < textLines.get(textLineNumber).getStyledCharacters().size()) {
            for (int i = index; i < textLines.get(textLineNumber).getStyledCharacters().size(); i++) {
                characters.add(new StyledCharacter(textLines.get(textLineNumber).getStyledCharacters().get(i).toChar()));
                textLines.get(textLineNumber).getStyledCharacters().remove(i);
                i--;
            }
        }
        for (int i = 0; i < cutCopy.size(); i++) {
            if (i == 0) {
                for (int j = 0; j < cutCopy.get(i).getStyledCharacters().size(); j++) {
                    this.addCharacter(new StyledCharacter(cutCopy.get(i).getStyledCharacters().get(j).toChar()), index + j);

                }
            } else {
                for (int j = 0; j < cutCopy.get(i).getStyledCharacters().size(); j++) {
                    this.addCharacter(new StyledCharacter(cutCopy.get(i).getStyledCharacters().get(j).toChar()), j);
                }
            }

            if (i != cutCopy.size() - 1) {
                textLines.add(textLineNumber + 1, new TextLine());
                textLineNumber++;
            } else {
                int size = textLines.get(textLineNumber).getStyledCharacters().size() * 7;// 7 = width of each character in monospaced font
                noteFrame.getBlinker().setX(size + noteFrame.LINE_BEGINNER);
                noteFrame.getBlinker().setY(50 + this.getTextLineNumber() * 20);
                for (int j = 0; j < characters.size(); j++) {
                    this.addCharacter(new StyledCharacter(characters.get(j).toChar()), textLines.get(textLineNumber).getStyledCharacters().size());
                }
            }
        }
//        if (first) {
//            textLines.get(0).getStyledCharacters().remove(textLines.get(0).getStyledCharacters().size() - 1);
//        }
//        deselect();
        if (selectAll) {
            selectAll = false;
        }
//        noteFrame.repaint();

    }

    public void cut() {
        cutCopy.clear();
        ArrayList<TextLine> selected = new ArrayList<>();
        for (int i = 0; i < textLines.size(); i++) {
            TextLine temp = new TextLine();
            for (int j = 0; j < textLines.get(i).getStyledCharacters().size(); j++) {
                if (textLines.get(i).getStyledCharacters().get(j).isSelected())
                    temp.getStyledCharacters().add(new StyledCharacter(textLines.get(i).getStyledCharacters().get(j).toChar()));
            }
//            if (temp.getStyledCharacters().size() != 0) {
            selected.add(temp);
//            }
        }
        cutCopy = selected;
        noteFrame.removeSelected();
    }

    public int findNextSpace() {
        int index = noteFrame.getBlinker().index();
        return this.textLines.get(textLineNumber).findNextSpace(index) + 1;
    }

    public int findPreviousSpace() {
        int index = noteFrame.getBlinker().index();
        return textLines.get(textLineNumber).findPreviousSpace(index);
    }

    public boolean isWordWrapMode() {
        return wordWrapMode;
    }

    public void setWordWrapMode(boolean wordWrapMode) {
        this.wordWrapMode = wordWrapMode;
    }

}
