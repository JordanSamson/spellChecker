package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.*;

public class TextFile implements IDataAccessor{
    private ObservableList<Word> wordsList;
    private ObservableList<Word> invalidWordsList = FXCollections.observableArrayList();

    public TextFile(){
        wordsList = FXCollections.observableArrayList();
    }

    public void specificFiller(Scanner sc) {
        wordsList.clear();
        int j=0;
        while (sc.hasNextLine()) {
            List<String> lineWords = Arrays.asList(sc.nextLine().trim().split("\\s+|(?=[^a-zA-Z])"));
            for(int i=0; i != lineWords.size(); ++i, ++j){
                wordsList.add(new Word(j, lineWords.get(i)));
            }
        }
    }

    public ObservableList<String> getTextList(){
        ObservableList<String> wordValueList = FXCollections.observableArrayList();
        wordsList.forEach(x -> wordValueList.add(x.getWord()));
        return wordValueList;
    }

    public synchronized void setInvalidWords(List<Word> invalidWords){
        invalidWordsList.addAll(invalidWords);
    }

    public synchronized void addInvalidWord(Word invalidWord){
        invalidWordsList.add(invalidWord);
    }

    public ObservableList<Word> getInvalidWordsList() {
        return invalidWordsList;
    }

    public void clearInvalidWordsList(){
        invalidWordsList.clear();
    }

    public void updateWord(Main main, Word oldWord, String newWord){
        for(int i=0; i != wordsList.size(); ++i) {
            if(wordsList.get(i).equals(oldWord))
            {
                wordsList.get(i).setWord(newWord);
                updateInvalidWords(main);
                return;
            }
        }
    }

    private void updateInvalidWords(Main main){
        main.threadedSpellChecking();
    }
}