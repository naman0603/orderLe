package com.example.orderleapp.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class


DBHelper extends SQLiteOpenHelper {

    public static Context context;
    public static SQLiteDatabase db = null;
    public static final int DATABASE_VERSION = 36;
    private static final String DB_PATH_SUFFIX = "/databases/";
    private static final String DATABASE_NAME = Config.DB_NAME;

    public DBHelper(Context context) {
        super(context, Config.DB_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion) {
            boolean isCheckSize = columnExistsInTable(db, "cart", "size");
            if (!isCheckSize) {
                //if isCheckWarehouseLocationCode is available then WarehouseLocationQty and ProductSerialNo also available
                db.execSQL("ALTER TABLE 'cart' ADD COLUMN 'size' INTEGER");
            }

            boolean isGoldType = columnExistsInTable(db, "cart", "gold_carat");
            if (!isGoldType){
                db.execSQL("ALTER TABLE 'cart' ADD COLUMN 'gold_carat' TEXT");
            }

        }
    }

    public static boolean columnExistsInTable(SQLiteDatabase db, String table, String columnToCheck) {
        Cursor cursor = null;
        try {
            //query a row. don't acquire db lock
            cursor = db.rawQuery("SELECT * FROM " + table + " LIMIT 0", null);

            // getColumnIndex()  will return the index of the column
            //in the table if it exists, otherwise it will return -1
            if (cursor.getColumnIndex(columnToCheck) != -1) {
                //great, the column exists
                return true;
            } else {
                //sorry, the column does not exist
                return false;
            }

        } catch (SQLiteException Exp) {
            //Something went wrong with SQLite.
            //If the table exists and your query was good,
            //the problem is likely that the column doesn't exist in the table.
            return false;
        } finally {
            //close the db  if you no longer need it
            // if (db != null) db.close();
            //close the cursor
            if (cursor != null) cursor.close();
        }
    }

    private static void copyDataBase() {

        try {
            // Open your local db as the input stream
            InputStream myInput = context.getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();

            // if the path doesn't exist first, create it
            File f = new File(context.getApplicationInfo().dataDir
                    + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkDatabase() {
        SQLiteDatabase checkDB = null;

        checkDB = null;
        try {
            try {
                String myPath = getDatabasePath();
                File file = new File(myPath);
                if (file.exists() && !file.isDirectory()) {
                    checkDB = SQLiteDatabase.openDatabase(myPath, null,
                            SQLiteDatabase.OPEN_READONLY);
                    checkDB.close();
                }
            } catch (Exception e) {
            }
        } catch (Throwable ex) {
        }
        return checkDB != null ? true : false;
    }

    private static String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public void execute(String statment) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(statment);
        } catch (Exception e) {
            Log.error(this.getClass() + " :: execute() ::", e);
            Log.print(e);
        } finally {
            db.close();
            db = null;
        }
    }

    public Cursor query(String statment) {
        Cursor cur = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            cur = db.rawQuery(statment, null);
            cur.moveToPosition(-1);
        } catch (Exception e) {
            Log.error(this.getClass() + " :: query() ::", e);
            Log.print(e);
        } finally {

            db.close();
            db = null;
        }

        return cur;
    }

    public static String getDBStr(String str) {

        str = str != null ? str.replaceAll("'", "''") : null;
        str = str != null ? str.replaceAll("&#039;", "''") : null;
        str = str != null ? str.replaceAll("&amp;", "&") : null;

        return str;

    }

    public void upgrade() {
        int level = Integer.parseInt(Pref.getValue(context, "LEVEL", "0"));
        switch (level) {
            case 0:
                doUpdate1();
            case 1:
                // doUpdate2();
            case 2:
                // doUpdate3();
            case 3:
                // doUpdate4();
        }
    }

    private void doUpdate1() {
//It's use for copy db from asset folder
//        if (DBHelper.db == null || !DBHelper.db.isOpen()) {
//
//            if (!checkDatabase()) {
//                copyDataBase();
//            }
//
//            DBHelper.db = this.getWritableDatabase();
//            DBHelper.db.enableWriteAheadLogging();
//        }

        this.execute("CREATE TABLE IF NOT EXISTS notification (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, message TEXT, type TEXT, date TEXT, time TEXT)");
        this.execute("CREATE TABLE IF NOT EXISTS cart (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, product_id INTEGER, product_name TEXT, product_picture TEXT, category_name TEXT, created_date TEXT, product_weight DOUBLE, final_weight DOUBLE, product_description TEXT, qty INTEGER,size TEXT, gold_carat TEXT)");

    }
}