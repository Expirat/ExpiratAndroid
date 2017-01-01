package com.expirate.expirat.model;


import android.provider.BaseColumns;

public class TypesContract {

    public static String TYPE_TEXT = " TEXT";

    public static String TYPE_INTEGER = " INTEGER";

    public static String COMMA_SEPARATED = ", ";

    public static class Types implements BaseColumns {
        public static String TABLE_NAME = "types_table";

        public static String COLUMN_NAME_TYPES_NAME = "types_name";



        public static String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + TYPE_INTEGER + " PRIMARY KEY" + COMMA_SEPARATED
                + COLUMN_NAME_TYPES_NAME + TYPE_TEXT + ")";

        public static String DROP_QUERY = "DROP TABLE " + TABLE_NAME;

        public static String INSERT_DEFAULT_QUERY =
                "INSERT INTO " + TABLE_NAME
                        + "(" + COLUMN_NAME_TYPES_NAME + ")"
                        + " VALUES"
                        + "('Groceries')" + COMMA_SEPARATED
                        + "('Cards')" + COMMA_SEPARATED
                        + "('Cosmetics')";
    }
}
