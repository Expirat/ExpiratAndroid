package com.expirate.expirat.model;


import android.provider.BaseColumns;

public class GroceriesContract {

    public static String TYPE_TEXT = " TEXT";

    public static String TYPE_INTEGER = " INTEGER";

    public static String COMMA_SEPARATED = ", ";

    public static class Groceries implements BaseColumns {
        public static String TABLE_NAME = "groceries_table";
        public static String COLUMN_NAME_NAME = "name";
        public static String COLUMN_NAME_BARCODE = "barcode";
        public static String COLUMN_NAME_BUY_DATE = "buy_date";
        public static String COLUMN_NAME_EXPIRE_DATE = "expired_date";
        public static String COLUMN_NAME_CREATED_DATE = "created_date";
        public static String COLUMN_NAME_MODIFIED_DATE = "modified_date";
        public static String COLUMN_NAME_TYPE_ID = "type_id";

        public static String CREATE_QUERY =
                "CREATE TABLE " + TABLE_NAME + "("
                        + _ID + TYPE_INTEGER + " PRIMARY KEY" + COMMA_SEPARATED
                        + COLUMN_NAME_NAME + TYPE_TEXT + COMMA_SEPARATED
                        + COLUMN_NAME_BARCODE + TYPE_TEXT + COMMA_SEPARATED
                        + COLUMN_NAME_BUY_DATE + TYPE_INTEGER + COMMA_SEPARATED
                        + COLUMN_NAME_EXPIRE_DATE + TYPE_INTEGER + COMMA_SEPARATED
                        + COLUMN_NAME_CREATED_DATE + TYPE_INTEGER + COMMA_SEPARATED
                        + COLUMN_NAME_TYPE_ID + TYPE_INTEGER + COMMA_SEPARATED
                        + COLUMN_NAME_MODIFIED_DATE + TYPE_INTEGER + ")";

        public static String DROP_QUERY = "DROP TABLE " + TABLE_NAME;

        public static String ALTER_TYPE_ID_QUERY = "ALTER TABLE " + TABLE_NAME
                + " ADD COLUMN " + COLUMN_NAME_TYPE_ID + TYPE_INTEGER + " DEFAULT 1";
    }
}
