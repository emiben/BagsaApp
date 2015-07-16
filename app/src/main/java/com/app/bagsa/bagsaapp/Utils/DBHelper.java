package com.app.bagsa.bagsaapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Emilino on 15/07/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private String 				sqlCreate;
    private String 				sqlUpdate;
    private SQLiteDatabase 		db;
    private SQLiteStatement stm;
    public static final int 	READ_ONLY = 0;
    public static final int 	READ_WRITE = 1;
    public static final String 	DB_NAME = "bagsa";
    public static final String 	DB_DIRECTORY = "DataBase";
    public static final int 	DB_VERSION = 1;
    private Context 			ctx;

    private Connection _Connection = null;
    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DBHelper(Context ctx){
        super(ctx, Env.getDB_PathName(ctx), null, DB_VERSION);
        this.ctx = ctx;
    }

    public static String getDB_Path() {
        return File.separator +
                Env.APP_DIRECTORY +
                File.separator +
                DB_DIRECTORY;
    }

    public static String getDB_PathName() {
        return File.separator +
                Env.APP_DIRECTORY +
                File.separator +
                DB_DIRECTORY +
                File.separator +
                DB_NAME;
    }

    public SQLiteDatabase openDB(int type){
        if(type == READ_ONLY){
            db = getReadableDatabase();
        }else if(type == READ_WRITE){
            db = getWritableDatabase();
        }
        return db;
    }

    /**
     * Get SQL with parameters
     * @author OpenUp
     * @param sql
     * @param values
     * @return
     * @return Cursor
     */
    public Cursor querySQL(String sql, String [] values){
        //LogM.log(ctx, getClass(), Level.FINE, "SQL=" + sql);
        return db.rawQuery(sql, values);
    }

    /**
     * Close DB
     * @author OpenUp
     * @param rs
     * @return void
     */
    public void closeDB(Cursor rs){
        if(rs != null && !rs.isClosed())
            rs.close();
        db.close();
        //LogM.log(ctx, getClass(), Level.INFO, "Closed");
    }

    /**
     * Update table
     * @author OpenUp
     * @param table
     * @param values
     * @param where
     * @param argmWhere
     * @return void
     */
    public void updateSQL(String table, ContentValues values, String where, String [] argmWhere){
        db.update(table, values, where, argmWhere);
    }

    /**
     * Execute SQL
     * @author OpenUp
     * @param sql
     * @return void
     */
    public void executeSQL(String sql){
        //LogM.log(ctx, getClass(), Level.FINE, "SQL=" + sql);
        db.execSQL(sql);
    }

    /**
     * Execute SQL with parameters
     * @author OpenUp
     * @param sql
     * @param param
     * @return void
     */
    public void executeSQL(String sql, Object [] param){
        //LogM.log(ctx, getClass(), Level.FINE, "SQL=" + sql);
        db.execSQL(sql, param);
    }

    /**
     * Insert on table
     * @author OpenUp
     * @param table
     * @param columnaNull
     * @param values
     * @return void
     */
    public long insertSQL(String table, String columnaNull, ContentValues values){
        return db.insert(table, columnaNull, values);
    }

    /**
     * Load a Connection
     * @author sbouissa 15/07/2015
     * @param conn
     * @param type
     * @return
     */
    public static void loadConnection(DBHelper conn, int type) {
        if(conn != null
                && !conn.isOpen()) {
            conn.openDB(type);
            if(type == READ_WRITE)
                conn.beginTransaction();
        }
    }

    /**
     * Verifi if is open database
     * @author sbouissa 15/07/2015
     * @return
     * @return boolean
     */
    public boolean isOpen(){
        boolean ok = false;
        if(db != null){
            ok = db.isOpen();
        }
        return ok;
    }

    /**
     * Begin transaction
     * @author sbouissa 15/07/2015
     * @return void
     */
    public void beginTransaction(){
        db.beginTransaction();
    }

    /**
     * End transaction
     * @author sbouissa 15/07/2015
     * @return void
     */
    public void endTransaction(){
        db.endTransaction();
    }

    public SQLiteDatabase getDB(){
        return db;
    }



    /**
     * Se recibe nombre de tabla a consultar clausula where (id = 123)
     * @param table
     * @param where
     * @return
     */
    public static boolean exists(String table, String where, Context ctxIn){
        boolean retValue = false;
        DBHelper conn = null;
        Cursor rs = null;
        try{
            String sql = "SELECT * FROM "+table+ " WHERE "+where;
            conn = new DBHelper(ctxIn);
            loadConnection(conn, READ_ONLY);
            rs = conn.querySQL(sql, null);
            if(rs.moveToFirst()) {
                retValue = true;
            }
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            conn.closeDB(rs);
        }
        //	Return
        return retValue;
    }


    /**
     * Se recibe nombre de tabla a consultar clausula where (id = 123)
     * @param table
     * @param cols
     * @return
     */
    public static boolean inserting(String table, String cols, String values, Context ctxIn){
        boolean retValue = false;
        DBHelper conn = null;
        Cursor rs = null;
        try{
            String sql = "INSERT INTO "+table+ " "+cols+" VALUES("+values+")";
            conn = new DBHelper(ctxIn);
            loadConnection(conn, READ_ONLY);
            conn.executeSQL(sql);
        }catch (SQLiteException e){
            e.getMessage();
        }finally {
            conn.closeDB(rs);
        }
        //	Return
        return retValue;
    }


}
