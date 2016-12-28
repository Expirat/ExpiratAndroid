package com.expirate.expirat.repository.groceries.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.expirate.expirat.model.GroceriesContract;
import com.expirate.expirat.repository.GroceriesDataSource;
import com.expirate.expirat.services.response.GroceriesItem;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;


public class LocalGroceriesData implements GroceriesDataSource {

    private static LocalGroceriesData INSTANCE;

    public static LocalGroceriesData newInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalGroceriesData(context);
        }
        return INSTANCE;
    }

    @NonNull
    private final BriteDatabase databaseHelper;

    @NonNull
    private Func1<Cursor, GroceriesItem> taskMapperCursorToListGroceries;

    private LocalGroceriesData(Context context) {
        checkNotNull(context, "Context cannot be null");

        GroceriesDBHelper groceriesDBHelper = new GroceriesDBHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        databaseHelper = sqlBrite.wrapDatabaseHelper(groceriesDBHelper, Schedulers.io());

        taskMapperCursorToListGroceries = this::getGroceriesList;
    }

    private GroceriesItem getGroceriesList(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(GroceriesContract.Groceries._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(
                GroceriesContract.Groceries.COLUMN_NAME_NAME));
        long buyDate = cursor.getLong(cursor.getColumnIndexOrThrow(
                GroceriesContract.Groceries.COLUMN_NAME_BUY_DATE));
        long expiredDate = cursor.getLong(cursor.getColumnIndexOrThrow(
                GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE));
        long createdDate = cursor.getLong(cursor.getColumnIndexOrThrow(
                GroceriesContract.Groceries.COLUMN_NAME_CREATED_DATE));
        long modifiedDate = cursor.getLong(cursor.getColumnIndexOrThrow(
                GroceriesContract.Groceries.COLUMN_NAME_MODIFIED_DATE));
        return GroceriesItem.builder()
                .setId(id)
                .setName(name)
                .setBuyDate(buyDate)
                .setExpiredDate(expiredDate)
                .setCreatedDate(createdDate)
                .setModifiedDate(modifiedDate)
                .build();
    }

    @Override
    public Observable<List<GroceriesItem>> getGroceriesList() {
        String[] projections = {
                GroceriesContract.Groceries._ID,
                GroceriesContract.Groceries.COLUMN_NAME_NAME,
                GroceriesContract.Groceries.COLUMN_NAME_BUY_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_CREATED_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_MODIFIED_DATE
        };
        String query = String.format("SELECT %s FROM %s ORDER BY %s ASC",
                TextUtils.join(", ", projections),
                GroceriesContract.Groceries.TABLE_NAME,
                GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE);

        return databaseHelper
                .createQuery(GroceriesContract.Groceries.TABLE_NAME, query)
                .mapToList(taskMapperCursorToListGroceries);
    }

    @Override
    public void saveGrocery(GroceriesItem groceriesItem) {
        checkNotNull(groceriesItem);

        ContentValues values = new ContentValues();
        values.put(GroceriesContract.Groceries.COLUMN_NAME_NAME, groceriesItem.name());
        values.put(GroceriesContract.Groceries.COLUMN_NAME_BUY_DATE, groceriesItem.buyDate());
        values.put(GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE,
                groceriesItem.expiredDate());
        values.put(GroceriesContract.Groceries.COLUMN_NAME_CREATED_DATE,
                groceriesItem.createdDate());
        values.put(GroceriesContract.Groceries.COLUMN_NAME_MODIFIED_DATE,
                groceriesItem.modifiedDate());

        databaseHelper.insert(GroceriesContract.Groceries.TABLE_NAME, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void deleteGrocerie(long id) {
        String where = GroceriesContract.Groceries._ID + " = ?";

        String[] whereArgs = { String.valueOf(id) };

        databaseHelper.delete(GroceriesContract.Groceries.TABLE_NAME, where, whereArgs);
    }

    @Override
    public void deleteGroceries() {
        databaseHelper.delete(GroceriesContract.Groceries.TABLE_NAME, null);
    }

    @Override
    public Observable<GroceriesItem> getGrocery(long id) {
        String[] projections = {
                GroceriesContract.Groceries._ID,
                GroceriesContract.Groceries.COLUMN_NAME_NAME,
                GroceriesContract.Groceries.COLUMN_NAME_BUY_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_CREATED_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_MODIFIED_DATE
        };

        String query = String.format("SELECT %s FROM %s WHERE %s = %o",
                TextUtils.join(", ", projections),
                GroceriesContract.Groceries.TABLE_NAME,
                GroceriesContract.Groceries._ID,
                id);

        return databaseHelper
                .createQuery(GroceriesContract.Groceries.TABLE_NAME, query)
                .mapToOne(taskMapperCursorToListGroceries);
    }

    @Override
    public void updateGrocery(long id, GroceriesItem groceriesItem) {
        checkNotNull(groceriesItem);

        ContentValues values = new ContentValues();
        values.put(GroceriesContract.Groceries.COLUMN_NAME_NAME, groceriesItem.name());
        values.put(GroceriesContract.Groceries.COLUMN_NAME_BUY_DATE, groceriesItem.buyDate());
        values.put(GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE,
                groceriesItem.expiredDate());
        values.put(GroceriesContract.Groceries.COLUMN_NAME_CREATED_DATE,
                groceriesItem.createdDate());
        values.put(GroceriesContract.Groceries.COLUMN_NAME_MODIFIED_DATE,
                groceriesItem.modifiedDate());

        String where = GroceriesContract.Groceries._ID + " = ?";

        String[] whereArgs = { String.valueOf(id) };

        databaseHelper.update(GroceriesContract.Groceries.TABLE_NAME, values, where, whereArgs);
    }

    @Override
    public Observable<List<GroceriesItem>> getAlmostExpiredGroceriesList() {
        return getGroceriesList()
                .filter(groceriesItems -> {

                    for (GroceriesItem groceriesItem : groceriesItems) {

                        long diff = (groceriesItem.expiredDate() * 1000)
                                - System.currentTimeMillis();

                        long dayDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

                        if (dayDiff > 10) {
                            groceriesItems.remove(groceriesItem);
                        }
                    }

                    return groceriesItems.size() > 0;
                });
    }
}
