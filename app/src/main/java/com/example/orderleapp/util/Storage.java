package com.example.orderleapp.util;

import android.annotation.SuppressLint;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Storage {

    public static void verifyLogPath() throws IOException {
        File dir = new File(Config.DIR_LOG);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = null;
    }

    public static void verifyAPPHomePath() throws IOException {
        File dir = new File(Config.APP_HOME);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = null;
    }

    public static void verifyUserDataPath() throws IOException {
        verifyAPPHomePath();
        File dir = new File(Config.DIR_USERDATA);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = null;
    }

    public static void verifyImagePath() throws IOException {
        verifyAPPHomePath();
        File dir = new File(Config.DIR_IMAGES);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = null;
    }

    public static void verifyEqualizerPath() throws IOException {
        verifyAPPHomePath();
        File dir = new File(Config.DIR_EQUALIZER);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = null;
    }

    public static void verifyMusicPath() throws IOException {
        verifyAPPHomePath();
        File dir = new File(Config.DIR_RINGTONE);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = null;
    }

    public static void verifyTempImagePath() throws IOException {
        verifyAPPHomePath();
        File dir = new File(Config.DIR_IMAGES_TEMP);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = null;
    }

    public static void verifyVideoPath() throws IOException {
        verifyAPPHomePath();
        File dir = new File(Config.DIR_VIDEO);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = null;
    }

    @SuppressLint("SimpleDateFormat")
    public static File verifyLogFile() throws IOException {
        File logFile = new File(Config.DIR_LOG + "/Clik_Log_"
                + new SimpleDateFormat("yyyy_MM_dd").format(new Date())
                + ".html");
        FileOutputStream fos = null;

        Storage.verifyLogPath();

        if (!logFile.exists()) {
            logFile.createNewFile();

            fos = new FileOutputStream(logFile);

            String str = "<TABLE style=\"width:100%;border=1px\" cellpadding=2 cellspacing=2 border=1><TR>"
                    + "<TD style=\"width:30%\"><B>Date n Time</B></TD>"
                    + "<TD style=\"width:20%\"><B>Title</B></TD>"
                    + "<TD style=\"width:50%\"><B>Description</B></TD></TR>";

            fos.write(str.getBytes());
        }

        if (fos != null) {
            fos.close();
        }

        fos = null;

        return logFile;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static void clearLog() {
        String files[] = null;
        File file = null;
        try {
            files = new File(Config.DIR_LOG).list();

            for (int ele = 0; ele < files.length; ele++) {
                file = new File(Config.DIR_LOG, files[ele]);

                file.delete();
            }

        } catch (Exception e) {
            Log.error(Storage.class + " :: clearLog :: ", e);
        }

        files = null;
        files = null;
    }

    public static void copy(String src, String dest) {
        FileInputStream fis = null;
        FileOutputStream out = null;
        int ch;

        try {
            fis = new FileInputStream(src);
            out = new FileOutputStream(dest);
            while ((ch = fis.read()) != -1) {
                out.write(ch);
            }

            fis.close();
            out.close();
        } catch (IOException e) {
            Log.error("Storage::copy()", e);
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
            }

            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }

        fis = null;
        out = null;
    }

    public static void copyDB(Context context) {
        Storage.copy("/data/data/" + context.getPackageName() + "/databases/"
                + Config.DB_NAME, Config.APP_HOME + "/" + Config.DB_NAME);
    }

}