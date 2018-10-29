package matcha.kay.jp.simplehouseholdaccountbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    public MyDatabaseOpenHelper(Context context) {
        super(context, "kadai04", null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //table作成
        String sql = "CREATE TABLE t_bop ("
                + "bop_name text primary key,"
                + "i_or_s integer not null)";
        db.execSQL(sql);

        sql = "CREATE TABLE t_income_and_spending ("
                + "_id          integer   primary key autoincrement,"
                + "money        integer   not null,"
                + "bop_name     text      not null,"
                + "year         integer   not null,"
                + "month        integer   not null,"
                + "dayOfMonth   integer   not null,"
                + "foreign key(bop_name) REFERENCES t_bop(bop_name) on DELETE CASCADE)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
