package com.adorgolap.ecode.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by ifta on 10/3/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = null;

    private static String DB_NAME = "eCode.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        File databasePath = myContext.getDatabasePath(DB_NAME);
        return databasePath.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLiteException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ECodeData searchByECode(String eCode)
    {
        String query = "Select * from E_CODE_INFO where code = \'"+eCode+"\'";
        Cursor c = myDataBase.rawQuery(query,null);
        if(c.moveToFirst())
        {
            String code = c.getString(c.getColumnIndex("code"));
            String name = c.getString(c.getColumnIndex("name"));
            String description = c.getString(c.getColumnIndex("description"));
            String halalStatus = c.getString(c.getColumnIndex("halalStatus"));
            return  new ECodeData(code,name,description,halalStatus);
        }
        return null;
    }
    public ArrayList<String> getEcodes(){
        ArrayList<String> eCodes = new ArrayList<String>();
        String query = "Select code from E_CODE_INFO where code like \'E%\'";
        Cursor c = myDataBase.rawQuery(query,null);
        if(c.moveToFirst())
        {
            do {
                eCodes.add(c.getString(c.getColumnIndex("code")));
            }while (c.moveToNext());
//            return eCodes;
        }
        query = "Select code from E_CODE_INFO where code not like \'E%\'";
        c = myDataBase.rawQuery(query,null);
        if(c.moveToFirst())
        {
            do {
                eCodes.add(c.getString(c.getColumnIndex("code")));
            }while (c.moveToNext());
            return eCodes;
        }
        return null;
    }
}
