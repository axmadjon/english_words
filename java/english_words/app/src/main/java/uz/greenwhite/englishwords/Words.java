package uz.greenwhite.englishwords;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"Since15", "WeakerAccess"})
class Words {

    private static final String word_txt = "words.csv",
            eStats_txt = "eStats.csv",
            tStats_txt = "tStats.csv",
            bookmark_txt = "bookmark.csv",
            logs_txt = "log.txt";

    private ArrayList<Word> words = new ArrayList<>();
    private final HashMap<String, Integer> eStats = new HashMap<>(), tStats = new HashMap<>();
    private final Set<String> bookmark = new HashSet<>();
    private PrintWriter eStatsWriter, tStatsWriter;

    //----------------------------------------------------------------------------------------------

    public ArrayList<Word> getWords() {
        return words;
    }


    //----------------------------------------------------------------------------------------------

    private void removeWordIfBookmark() {
        if (!bookmark.isEmpty()) {
            ArrayList<Word> clone = (ArrayList<Word>) words.clone();
            words.clear();
            for (Word w : clone) {
                if (!bookmark.contains(w.english)) {
                    words.add(w);
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------

    public void setTStats(String word, Integer sign) {
        tStatsWriter = Util.setStats(tStatsWriter, tStats, tStats_txt, word, sign);
    }

    public void randomiseT() {
        removeWordIfBookmark();
        words = Util.sort(words, tStats);
    }

    //----------------------------------------------------------------------------------------------

    public void setEStats(String word, Integer sign) {
        eStatsWriter = Util.setStats(eStatsWriter, eStats, eStats_txt, word, sign);
    }

    public void randomiseE() {
        removeWordIfBookmark();
        words = Util.sort(words, eStats);
    }

    //----------------------------------------------------------------------------------------------

    void load() {
        words.clear();
        LineReader.reader(bookmark_txt, new LineReader() {
            @Override
            public void read(String key, String value, String reader, String text) {
                bookmark.add(key);
            }
        });

        LineReader.reader(word_txt, new LineReader() {
            @Override
            public void read(String key, String value, String reader, String text) {
                if (!bookmark.contains(key)) {
                    words.add(new Word(key, value, reader, text));
                }
            }
        });

        LineReader.reader(eStats_txt, new LineReader() {
            @Override
            public void read(String key, String value, String reader, String text) {
                Util.addStats(eStats, key, Integer.parseInt(value));
            }
        });

        LineReader.reader(tStats_txt, new LineReader() {
            @Override
            public void read(String key, String value, String reader, String text) {
                Util.addStats(tStats, key, Integer.parseInt(value));
            }
        });
    }

    public void saveBookmark(String text) {
        File file = new File(LineReader.getRootPath() + "/" + bookmark_txt);
        try {
            PrintWriter w = new PrintWriter(new FileWriter(file, true));
            w.println(text);
            w.println();
            w.flush();
            w.close();
            bookmark.add(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLog(String log) {
        File file = new File(LineReader.getRootPath() + "/" + logs_txt);
        try {
            PrintWriter w = new PrintWriter(new FileWriter(file, true));
            w.println(log);
            w.println();
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
