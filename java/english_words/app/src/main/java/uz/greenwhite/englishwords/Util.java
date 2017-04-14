package uz.greenwhite.englishwords;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@SuppressWarnings("ResultOfMethodCallIgnored")
class Util {

    public static <A> A nvl(A v, A d) {
        return v != null ? v : d;
    }

    private static ArrayList<Word> random(ArrayList<Word> words) {
        if (words.isEmpty()) return words;
        ArrayList<Word> result = new ArrayList<>();
        Random random = new Random();
        int lastNumber = -1;
        while (words.size() > 1) {
            int randomNumber = random.nextInt(words.size());
            if (lastNumber != -1 && lastNumber == randomNumber) {
                continue;
            }
            lastNumber = randomNumber;
            result.add(words.get(randomNumber));
            words.remove(randomNumber);
        }
        result.add(words.get(0));
        return result;
    }

    @SuppressWarnings("Since15")
    static ArrayList<Word> sort(ArrayList<Word> words, final HashMap<String, Integer> stats) {
        long start = System.currentTimeMillis();
        ArrayList<Word> yes = new ArrayList<>();
        ArrayList<Word> no = new ArrayList<>();
        ArrayList<Word> un = new ArrayList<>();
        for (Word w : words) {
            int n = nvl(stats.get(w.english), new Integer(0));
            if (n == 0) un.add(w);
            else if (n > 0) yes.add(w);
            else if (n < 0) no.add(w);
        }
        System.out.println("group:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        no = random(no);
        System.out.println("no:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        no.addAll(random(un));
        System.out.println("un:" + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        no.addAll(random(yes));
        System.out.println("yes:" + (System.currentTimeMillis() - start));

        return no;
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
