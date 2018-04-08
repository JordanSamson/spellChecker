package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.concurrent.locks.ReentrantLock;


public class Main extends Application implements EventHandler<ActionEvent> {
    Stage mainStage;
    Dictionary dictionary = new Dictionary();
    TextFile textFile = new TextFile();
    TextField correctionInput;
    TableView<Word> tableView;
    StringProperty correctionWord = new SimpleStringProperty(this, "CorrectionWord", "");

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        primaryStage.setTitle("Spell Checker");
        generateLayout();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void generateLayout(){
        HBox topMenu = new HBox();
        HBox bottomMenu = new HBox();
        VBox vBoxWindow = new VBox();
        vBoxWindow.setPadding(new Insets(10));

        TextArea textArea = new TextArea();
        StackPane layout = new StackPane();
        tableView = new TableView<>();
        ListView<String> dict = new ListView<>();

        // Top menu buttons
        Button button = new Button("Import dictionary");
        button.setOnAction(e -> dictionaryHandler());
        Button button2 = new Button("Import text file");
        button2.setOnAction(e -> textFileHandler(textArea));
        Button buttonChecker = new Button("Verify text");
        buttonChecker.setOnAction(e -> checkerHandler());
        Button buttonSaveFile = new Button("Save file");
        buttonSaveFile.setOnAction(e -> saveFileHandler());
        Button buttonChangeWord = new Button("Apply");
        buttonChangeWord.setOnAction(e -> changeSpellingHandler(textArea));

        //Input field
        correctionInput = new TextField();
        correctionInput.setPromptText("Word to correct");

        //TextArea
        textArea.setWrapText(true);
        textArea.maxHeight(400);
        textArea.setEditable(false);
        textArea.maxWidth(250);

        tableView = createTable();
        dict = createListView();

        //RightSide Box
        VBox rightSide = new VBox();
        rightSide.minWidth(200);
        rightSide.getChildren().addAll(tableView, correctionInput, buttonChangeWord);


        //Top menu
        topMenu.setPadding(new Insets(10));
        topMenu.setSpacing(10);
        topMenu.getChildren().addAll(buttonSaveFile, button,button2, buttonChecker);

        //Bottom menu
        bottomMenu.setPadding(new Insets(10));
        bottomMenu.setSpacing(10);
        bottomMenu.getChildren().addAll(dict, textArea, rightSide);

        vBoxWindow.getChildren().addAll(topMenu, bottomMenu);

        mainStage.setScene(new Scene(vBoxWindow, 800, 500));
        mainStage.setResizable(false);
        mainStage.show();

        //return layout;
    }

    private TableView createTable(){
        TableView<Word> tv = new TableView<>();

        //Columns
        TableColumn<Word, Integer> indexColumn = new TableColumn<>("Index");
        TableColumn<Word, String> wordColumn = new TableColumn<>("Word");
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));

        tv.setItems(textFile.getInvalidWordsList());
        tv.getColumns().addAll(indexColumn, wordColumn);

        tv.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> { if(newValue != null){ correctionInput.setText(newValue.getWord());} });

        return tv;

    }

    private ListView<String> createListView(){
        ListView<String> lv = new ListView<>();
        lv.maxWidth(100);
        lv.setItems(dictionary.getDictionaryValues());
        return lv;
    }

    private void textFileHandler(TextArea textUpdate){
        FileManager.populateValues(FileManager.getTextFile("text file", mainStage), x -> textFile.specificFiller(x));
        textUpdate.setText(String.join(" ", textFile.getTextList()));
    }

    private void dictionaryHandler(){
        FileManager.populateValues(FileManager.getTextFile("dictionary", mainStage), x -> dictionary.specificFiller(x));
    }

    private void checkerHandler(){
        if(textFile.getTextList().size() <= 0)
            return;
        //textFile.clearInvalidWords();
        synchronized (this) {
            threadedSpellChecking();
        }
    }

    public void threadedSpellChecking() {
        int maxNumberOfThreads = 8;
        textFile.clearInvalidWordsList();

        TextChecker.setDictionary(dictionary);
        TextChecker.setTextFile(textFile);

        int minDiv = Math.min(maxNumberOfThreads, textFile.getTextList().size());
        int increment = textFile.getTextList().size() / minDiv;
        int diff=0;
        if(minDiv == maxNumberOfThreads){
            diff = textFile.getTextList().size() - (increment*minDiv);
        }
        for (int i = 0; i < minDiv; ++i) {
            new Thread(new TextChecker(i * increment, (i + 1) * increment, textFile.getTextList())).start();
        }
        if(diff !=0){
            new Thread(new TextChecker(minDiv * increment, minDiv * increment + diff, textFile.getTextList())).start();
        }
    }

    private void changeSpellingHandler(TextArea text){
        textFile.updateWord(this, tableView.getSelectionModel().selectedItemProperty().getValue(), correctionInput.getText());
        text.setText(String.join(" ", textFile.getTextList()));
    }

    private void saveFileHandler(){
        if(textFile.getTextList().size() <= 0)
            return;
        FileManager.saveFile(textFile, mainStage);
    }

    @Override
    public void handle(ActionEvent event) {

    }
}