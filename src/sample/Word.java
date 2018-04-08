package sample;

public class Word {
    private int index;
    private String word;

    public Word(String word){
        this.word = word;
    }

    public Word(int index, String word){
        this.index = index;
        this.word = word;
    }

    public int getIndex(){
        return index;
    }

    public String getWord(){
        return word;
    }

    public void setWord(String newWord){
        word = newWord;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Word))return false;
        Word otherWord = (Word)other;
        if(otherWord.getWord() == this.getWord() &&
            otherWord.getIndex() == this.getIndex())
            return true;
        return false;
    }
}
