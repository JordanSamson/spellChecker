package sample;

import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.util.Scanner;

import javafx.stage.Stage;

public class FileManager{

    public static File getTextFile(String fileType, Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select "+ fileType);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile;
    }

    public static void populateValues(File file, IDataAccessor accessor){
        String[] encodings = {"UTF-8", "ISO-8859-9"};
        Scanner sc = null;
        for(int i=0; i != encodings.length; ++i) {
            Boolean exception = false;
            try {
                sc = new Scanner(file, encodings[i]);
                accessor.specificFiller(sc);

                // note that Scanner suppresses exceptions
                if (sc.ioException() != null) {
                    throw sc.ioException();
                }
            }
            catch(NullPointerException e){

            }
            catch(MalformedInputException e){
                exception = true;
            }
            catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (sc != null) {
                    sc.close();
                }

                // Only if no exceptions for illegal charsets
                if(!exception)
                    break;
            }
        }
    }

    public static void saveFile(TextFile fileText, Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                FileWriter fileWriter = null;

                fileWriter = new FileWriter(file);
                fileWriter.write(String.join(" ", fileText.getTextList()));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}