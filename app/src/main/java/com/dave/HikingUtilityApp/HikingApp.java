package com.dave.HikingUtilityApp;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HikingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupCrashHandler();
    }

    private void setupCrashHandler() {
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                String stackTrace = sw.toString();

                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK).format(new Date());

                File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File crashFile = new File(downloads, "crash.txt");

                FileWriter fw = new FileWriter(crashFile, false);
                fw.write("=== HIKING APP CRASH REPORT ===\n");
                fw.write("Time: " + time + "\n");
                fw.write("Thread: " + thread.getName() + "\n\n");
                fw.write("Exception: " + throwable.getMessage() + "\n\n");
                fw.write("Stack Trace:\n");
                fw.write(stackTrace);
                fw.close();

            } catch (Exception ignored) {}

            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
            }
        });
    }
}
