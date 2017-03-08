package com.nimrag.kevin.aweweico.lib;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 2017/3/8.
 * 写log到文件
 */

public class Logger2File {

    public static boolean DEBUG = Logger.DEBUG;
    private static LoggerThread mThread;
    private static Calendar mCalendar;

    public static void log2File(String tag, String log) {
        if (!DEBUG) {
            return;
        }

        try {
            if (GlobalContext.getInstance() != null) {
                LoggerThread thread = getThread(GlobalContext.getInstance());
                thread.addLog(new Log(tag, log));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static LoggerThread getThread(Context context) {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        String fileName = String.format("%s_%s_%s_%s.txt", mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1, mCalendar.get(Calendar.DAY_OF_MONTH), mCalendar.get(Calendar.HOUR_OF_DAY));
        if (mThread == null || !mThread.fileName.equals(fileName)) {
            mThread = new LoggerThread(context, fileName);
            mThread.start();
        }

        return mThread;
    }

    static class LoggerThread extends Thread {

        FileWriter fileWriter;
        String fileName;
        DateFormat formatter;
        LinkedBlockingDeque<Log> logsQueue;

        public LoggerThread(Context context, String fileName) {
            String filePath = context.getExternalFilesDir("logs").getAbsolutePath() + File.separator;
            this.fileName = fileName;
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            file = new File(filePath + File.separator + fileName);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                fileWriter = new FileWriter(file.getAbsolutePath(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            logsQueue = new LinkedBlockingDeque<Log>();
            formatter = new SimpleDateFormat();
        }

        void addLog(Log log) {
            if (fileWriter != null && logsQueue != null) {
                logsQueue.add(log);
            }
        }

        @Override
        public void run() {
            super.run();

            while (true) {
                try {
                    Log log = logsQueue.poll(30, TimeUnit.SECONDS);
                    if (log != null && fileWriter != null) {
                        String line = formatter.format(mCalendar.getTime()) + " " + log.tag + " " + log.log;
                        try {
                            fileWriter.write(line + "\n\r");
                            fileWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }

            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (mThread == this) {
                mThread = null;
            }
        }
    }

    private static class Log {
        String tag;
        String log;

        public Log(String tag, String log) {
            this.tag = tag;
            this.log = log;
        }
    }
}
