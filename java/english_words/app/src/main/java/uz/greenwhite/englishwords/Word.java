package uz.greenwhite.englishwords;

@SuppressWarnings("WeakerAccess")
public class Word {

    public final String english, translation, reader, text;

    public Word(String english, String translation,
                String reader, String text) {
        this.english = english;
        this.translation = translation;
        this.reader = reader;
        this.text = text;
    }

}
