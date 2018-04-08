package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class TextChecker implements Runnable{
    private static Dictionary dictionary;
    private static TextFile textFile;
    private int minData, maxData;
    private List<String> data;
    ReentrantLock lock = new ReentrantLock();

    public static void setDictionary(Dictionary p_dictionary){
        dictionary = p_dictionary;
    }
    public static void setTextFile(TextFile p_textFile) { textFile = p_textFile;}

    public TextChecker(int min,int max, List<String> data) {
        minData = min;
        maxData = max;
        this.data = data;
    }

    @Override
    public void run() {
        for(int i = minData; i < maxData; ++i) {
            try {
                if (!dictionary.getDictionaryValues().contains(data.get(i).toLowerCase()) && data.get(i).matches("[A-zÀ-ú]*")) {
                    lock.lock();
                    try {
                        textFile.addInvalidWord(new Word(i, data.get(i)));
                    } finally {
                        lock.unlock();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }


}
