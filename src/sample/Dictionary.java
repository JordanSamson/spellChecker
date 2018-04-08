package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Dictionary implements IDataAccessor{
    private ObservableList<String> dictionaryValues = FXCollections.observableArrayList();

    public void specificFiller(Scanner sc) {
        dictionaryValues.clear();
        while (sc.hasNextLine()) {
            dictionaryValues.add(sc.nextLine().trim().toLowerCase());
            java.util.Collections.sort(dictionaryValues); //Sort dictionary
        }
    }

    public ObservableList<String> getDictionaryValues(){
        return dictionaryValues;
    }
}