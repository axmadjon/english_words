package uz.greenwhite.englishwords;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

@SuppressWarnings("ResultOfMethodCallIgnored")
class Util {

    public static <A> A nvl(A v, A d) {
        return v != null ? v : d;
    }

    @SuppressWarnings("Since15")
    static void sort(ArrayList<Word> words, final HashMap<String, Integer> stats) {
        final Random random = new Random();
        Collections.sort(words, new Comparator<Word>() {
            @Override
            public int compare(Word r, Word l) {
                int rN = nvl(stats.get(r.english), new Integer(0));
                int lN = nvl(stats.get(l.english), new Integer(0));
                if (rN != lN) {
                    return rN - lN;
                } else {
                    return random.nextBoolean() ? 1 : -1;
                }
            }
        });
    }

    static void addStats(HashMap<String, Integer> stat, String word, Integer sign) {
        Integer integer = stat.get(word);
        if (integer == null) {
            integer = 0;
        }
        integer += sign;
        stat.put(word, integer);
    }

    static PrintWriter setStats(PrintWriter writer,
                                HashMap<String, Integer> stat,
                                String file, String word, Integer sign) {
        addStats(stat, word, sign);

        if (writer == null) {
            try {
                File f = new File(LineReader.getRootPath() + "/" + file);
                if (!f.exists()) f.createNewFile();
                writer = new PrintWriter(new FileWriter(f, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writer == null) return null;
        writer.println(word + ";" + sign);
        writer.flush();
        return writer;
    }
}
