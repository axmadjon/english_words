package uz.greenwhite.englishwords;

import android.app.Application;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ThreadExceptionHandler.register(new ThreadExceptionHandler.OnError() {
            @Override
            public void onError(Throwable ex) {
                Words.saveLog(extractStackTrace(ex));
            }
        });
    }

    public static String extractStackTrace(Throwable ex) {
        StringWriter result = new StringWriter();
        PrintWriter writer = new PrintWriter(result);
        ex.printStackTrace(writer);
        writer.close();
        return result.toString();
    }
}
