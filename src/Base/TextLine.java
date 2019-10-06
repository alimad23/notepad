package Base;


import java.util.ArrayList;

public class TextLine {

    private ArrayList<StyledCharacter> styledCharacters = new ArrayList<>();


    public TextLine() {
    }

    public ArrayList<StyledCharacter> getStyledCharacters() {
        return styledCharacters;
    }

    @Override
    public String toString() {
        String result = "";
        int i = 0;
        if (styledCharacters.size() != 0) {
//            while (styledCharacters.get(i).isHasValue()) {
//                result += styledCharacters.get(i).toChar();
//                i++;
//                if (i == styledCharacters.size()) break;
//            }
            for (int j = 0; j < styledCharacters.size(); j++) {
                if (styledCharacters.get(j).isHasValue()) {
                    result += styledCharacters.get(j).toChar();
                }
            }
        }
        return result;
    }

    public void add(StyledCharacter temp, int index) {
        if (temp.toChar() != '\u0001' && temp.toChar() != '\u0004' && temp.toChar() != '\u007F') {//u0001 = ctrl + a && u0004 = ctrl + d
            if (index <= styledCharacters.size()) {
                styledCharacters.add(index, temp);
                styledCharacters.get(index).setHasValue(true);
            } else {
                styledCharacters.add(temp);
                styledCharacters.get(styledCharacters.size() - 1).setHasValue(true);
            }
        }
    }

    protected int findNextSpace(int index) {
        for (int i = index; i < this.styledCharacters.size(); i++) {
            if (styledCharacters.get(i).toChar() == ' ') {
                if (i == styledCharacters.size() - 1) return i;
                while (styledCharacters.get(i + 1).toChar() == ' ') {
                    i++;
                    if (i == styledCharacters.size() - 1) return i;
                }
                return i;
            }
        }
        return styledCharacters.size() - 1;
    }

    public int findPreviousSpace(int index) {
        index--;
        while (styledCharacters.get(index).toChar() == ' ') {
            index--;
            if (index == -1) return -1;
        }
        for (int i = index; i >= 0; i--) {
            if (styledCharacters.get(i).toChar() == ' ') return i + 1;
        }
        return 0;

    }

}
