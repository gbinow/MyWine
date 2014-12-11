package gustavo.mywine.app.core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database {

    private SQLiteDatabase db = null;
    private Context context = null;

    private static final String TABLE_USER	 =
            "CREATE TABLE IF NOT EXISTS tbl_user ( "
            +"fld_id INTEGER NOT NULL PRIMARY KEY autoincrement ,"
            +"fld_login VARCHAR(60) NOT NULL,"
            +"fld_password VARCHAR(15) NOT NULL  "
            +");";

    private static final String TABLE_WINE	 =
            "CREATE TABLE IF NOT EXISTS tbl_wine ( "
                    +"fld_id INTEGER NOT NULL PRIMARY KEY autoincrement ,"
                    +"fld_label VARCHAR(50) NOT NULL,"
                    +"fld_year INT NOT NULL,"
                    +"fld_grape VARCHAR(50) NOT NULL,"
                    +"fld_rating INT NOT NULL,"
                    +"fld_observations VARCHAR(200) NOT NULL,"
                    +"tbl_user_fld_id INT NOT NULL"
                    +");";

    private static final String TABLE_RSS	 =
            "CREATE TABLE IF NOT EXISTS tbl_rss ( "
                    +"fld_id INTEGER NOT NULL PRIMARY KEY autoincrement ,"
                    +"fld_name VARCHAR(50) NOT NULL,"
                    +"fld_url VARCHAR(200) NOT NULL,"
                    +"tbl_user_fld_id INT NOT NULL"
                    +");";


    private static final String DATABASE = "wine";


    public Database(Context context)
    {
        this.context = context;

        connect();

        //dropTable("DROP TABLE tbl_user");
        //dropTable("DROP TABLE tbl_wine");
        //dropTable("DROP TABLE tbl_rss");

        createTable(TABLE_USER);
        createTable(TABLE_WINE);
        createTable(TABLE_RSS);

    }

    private void connect()
    {
        try {
            this.db = context.openOrCreateDatabase(DATABASE, 0, null);
            Log.i("DATABASE:", "Successfully Database initialized.");

        } catch (Exception e) {
            Log.e("DATABASE ERROR :",e.toString());
        }
    }

    private void createTable(String sql){
        try {
            this.db.execSQL(sql);
        } catch (Exception e) {
            Log.e("DATABASE ERROR:", "Error creating table." + sql);
        }
    }

    public void dropTable(String sql){
        try {
            this.db.execSQL(sql);
        } catch (Exception e) {
            Log.e("DATABASE ERROR:", "Error deleting table." + sql);
        }
    }

    public int existsColumn(String table, String column){

        // Return: -1 No table exists
        // Return: 0 There is no table but is registered
        // Return: There is one table, record, and has column with given name
        // Return: There is table 2, and no record has column with given name

        String query = "SELECT * FROM "+table+" LIMIT 1;";
        int result = -1;

        Cursor c = null;

        c = this.db.rawQuery(query, null);

        if(c !=null){

            result = 0;

            if(c.getCount()>0){
                result = 2;
            }

            while(c.moveToNext()){

                for(int i=0; i<c.getColumnCount(); i++){
                    if(c.getColumnName(i).equals(column)==true){
                        result = 1;
                    }
                }
            }

            c.close();
        }

        return(result);
    }

    public void execSQL(String sql){
        this.db.execSQL(sql);
    }

    public void executeInsert(String sql){

        try {
            this.db.execSQL(sql);
            Log.i("DATABASE ERROR:", "Insert sucess. " + sql);
        } catch (Exception e) {
            Log.e("DATABASE ERROR:", "Error insert." + e.toString());
        }
    }

    public Cursor executeQuery(String table_name,String[] fields, String condition, String order ){
        Cursor c = null;
        try {
            c = this.db.query(true, table_name, fields, condition, null, null, null, order, null);
        } catch (Exception e) {
            Log.e("DATABASE ERROR:", "Error execute query." + e.toString());
        }
        return (c);

    }

    public void executeDelete(String tableName,String condition){
        this.db.delete(tableName, condition, null);
    }

    public SQLiteDatabase getDb()
    {
        return this.db;
    }

    public void closeDataBase(){
        this.db.close();
    }
}
