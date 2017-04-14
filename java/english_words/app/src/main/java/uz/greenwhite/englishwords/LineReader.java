package uz.greenwhite.englishwords;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public abstract class LineReader {

    public abstract void read(String key, String value, String reader, String text);

    public static void reader(String fileName, LineReader reader) {
        File words = new File(getRootPath() + "/" + fileName);
        if (!words.exists()) {
            return;
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(words));
            String line;
            while ((line = in.readLine()) != null) {
                try {
                    String[] split = line.split(";");
                    String txt0 = split[0].trim();
                    String txt1 = split.length > 1 ? split[1].trim() : "";
                    String txt2 = split.length > 2 ? split[2].trim() : "";
                    String txt3 = split.length > 3 ? split[3].trim() : "";
                    reader.read(txt0, txt1, txt2, txt3);
                } catch (Exception e) {
                    e.printStackTrace();
                    Words.saveLog(line + "\n" + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getRootPath() {
        File root = new File(Environment.getExternalStorageDirectory(), "english_words");
        if (!root.exists()) root.mkdirs();
        return root.getAbsolutePath();
    }
}
