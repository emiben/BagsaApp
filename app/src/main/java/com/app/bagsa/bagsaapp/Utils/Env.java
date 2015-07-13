package com.app.bagsa.bagsaapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by SBT on 30/06/2015.
 */
public class Env {
    /**************************************************************************
     *  Application Context
     */

    private static final String	SET_ENV = "#SET_ENV#";


    private final String APP_BASE_FOLDER = "AppBagsa";
    private final String APP_DB_FOLDER = "DataBaseApp";
    private final String APP_DATA_FOLDER = "DataApp";

    /** Database Context*/
    public static final String		DB_VERSION = "#DB_Version";
    public static final String		DB_NAME = "#DB_Name";
    public static final String		APP_DIR_NAME = "#APP_DIR_Name";

    public static  final String DB_NAME_ASSETS = "bagsa"; //name of file in assets folder
    /******************************************************************************
     * App Context
     */
    public static final String 		APP_DIRECTORY = "AppBagsa";
    public static final String 		DOC_DIRECTORY = "Version";
    public static final String 		DOC_AUXILIAR = "Auxiliary";

    public static final String      DOC_QS = "QS";
    public static final String      ASSETS_QSOUT ="QSOUT" ;

    public static String getDB_PathName(Context ctx){
        return getContext(ctx, DB_NAME);
    }

    public static String getContext (Context ctx, String context)
    {
        if (ctx == null || context == null)
            throw new IllegalArgumentException ("Require Context");
        SharedPreferences pf = PreferenceManager.getDefaultSharedPreferences(ctx);
        return pf.getString(context, null);
    }	//	getContext

    public static boolean isEnvLoad(Context ctx){
        return getContextAsBoolean(ctx, SET_ENV);
    }

    public static boolean getContextAsBoolean (Context ctx, String context)
    {
        if (ctx == null || context == null)
            throw new IllegalArgumentException ("Require Context");

        String s = getContext(ctx, context);
        //
        return (s != null && s.equals("Y"));
    }	//	getContext

    public static void setDB_Path(Context ctx, String dbPathName) {
        // TODO Auto-generated method stub
        setContext(ctx, DB_NAME, dbPathName);
    }

    public static void setContext (Context ctx, String context, boolean value)
    {
        setContext(ctx, context, value ? "Y": "N");
    }	//	setContext

    public static void setContext (Context ctx, String context, String value){
        if (ctx == null || context == null)
            return;
        //
        if (value == null)
            value = "";
        SharedPreferences.Editor ep = getEditor(ctx);
        ep.putString(context, value);
        ep.commit();
    }	//	setContext

    public static void setContext(Context ctx, String context, int value) {
        // TODO Auto-generated method stub
        if (ctx == null || context == null)
            return;
        SharedPreferences.Editor ep = getEditor(ctx);
        ep.putString(context, String.valueOf(value));
        ep.commit();
    }	//	setContext

    private static SharedPreferences.Editor getEditor(Context ctx){
        SharedPreferences pf = PreferenceManager.getDefaultSharedPreferences(ctx);
        return pf.edit();
    }

    public static void setAppDirName(Context ctx, String value){
        setContext(ctx, APP_DIR_NAME, value);
    }

    public static void setIsEnvLoad(Context ctx, boolean value) {
        // TODO Auto-generated method stub
        setContext(ctx, SET_ENV, value);
    }


}
