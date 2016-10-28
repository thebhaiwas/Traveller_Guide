package sujeet.traveller_guide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler  extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE = "MyDB";
    private static final String TABLE = "Places";
    public static final String COL0 = "Id";
    public static final String COL1 = "Name";
    public static final String COL2 = "Address";
    public static final String COL3 = "Type";
    public static final String COLS[] = {COL0, COL1, COL2, COL3};

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =  "CREATE TABLE "+TABLE+
                " ( "+
                COL0+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL1+" TEXT, "+
                COL2+" TEXT, "+
                COL3+" TEXT "+
                " );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+TABLE+" ; ";
        db.execSQL(query);
        onCreate(db);
    }

    public void addRow(ContentValues cv) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE, null, cv);
        db.close();
    }

    public ArrayList<ContentValues> getRows() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cr = db.query(TABLE, COLS, null, null, null, null, null);
        ArrayList<ContentValues> places = new ArrayList<>();
        for(cr.moveToFirst();!cr.isAfterLast();cr.moveToNext()) {
            ContentValues cv = new ContentValues();
            cv.put(COL1, cr.getString(cr.getColumnIndex(COL1)));
            cv.put(COL2, cr.getString(cr.getColumnIndex(COL2)));
            cv.put(COL3, cr.getString(cr.getColumnIndex(COL3)));
            places.add(cv);
        }
        db.close();
        return places;
    }
}
