package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Dictionary implements IDataAccessor{
    private ObservableList<String> dictionaryValues = FXCollections.observableArrayList();

    public void specificFiller(Scanner sc) {
        dictionaryValues.clear();
        while (sc.hasNextLine()) {
            String dictionaryWord = sc.nextLine().trim().toLowerCase();
            if(dictionaryWord.matches("[A-zÀ-ú]+") && !dictionaryValues.contains(dictionaryWord)) {
                dictionaryValues.add(dictionaryWord);
            }
        }
        java.util.Collections.sort(dictionaryValues); //Sort dictionary
    }

    public ObservableList<String> getDictionaryValues(){
        return dictionaryValues;
    }
}