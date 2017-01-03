package com.expirate.expirat.repository.groceries.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.expirate.expirat.model.GroceriesContract;
import com.expirate.expirat.model.TypesContract;
import com.expirate.expirat.repository.GroceriesDataSource;
import com.expirate.expirat.services.response.Dashboards;
import com.expirate.expirat.services.response.GroceriesItem;
import com.expirate.expirat.services.response.TypesItem;
import com.expirate.expirat.utils.DateUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;


public class LocalGroceriesDataSource implements GroceriesDataSource {

    private static LocalGroceriesDataSource INSTANCE;

    public static LocalGroceriesDataSource newInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalGroceriesDataSource(context);
        }
        return INSTANCE;
    }

    @NonNull
    private final BriteDatabase databaseHelper;

    @NonNull
    private Func1<Cursor, GroceriesItem> taskMapperCursorToListGroceries;

    @NonNull
    private Func1<Cursor, TypesItem> taskMapperCursorToListTypes;

    private LocalGroceriesDataSource(Context context) {
        checkNotNull(context, "Context cannot be null");

        GroceriesDBHelper groceriesDBHelper = new GroceriesDBHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        databaseHelper = sqlBrite.wrapDatabaseHelper(groceriesDBHelper, Schedulers.io());

        taskMapperCursorToListGroceries = this::getGroceriesList;
        taskMapperCursorToListTypes = this::getTypesList;
    }

    private TypesItem getTypesList(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(TypesContract.Types._ID));
        String typesName = cursor.getString(
                cursor.getColumnIndexOrThrow(TypesContract.Types.COLUMN_NAME_TYPES_NAME));
        return TypesItem.create(id, typesName);
    }

    private GroceriesItem getGroceriesList(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(
                GroceriesContract.Groceries.TABLE_NAME + "." + GroceriesContract.Groceries._ID));
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
        String typeName = cursor.getString(cursor.getColumnIndexOrThrow(
                TypesContract.Types.COLUMN_NAME_TYPES_NAME));
        return GroceriesItem.builder()
                .setId(id)
                .setName(name)
                .setBuyDate(buyDate)
                .setExpiredDate(expiredDate)
                .setCreatedDate(createdDate)
                .setModifiedDate(modifiedDate)
                .setTypeName(typeName)
                .build();
    }

    @Override
    public Observable<List<GroceriesItem>> getGroceries(@Nullable Long typeId) {
        String[] projections = {
                GroceriesContract.Groceries.TABLE_NAME + "." + GroceriesContract.Groceries._ID,
                GroceriesContract.Groceries.COLUMN_NAME_NAME,
                GroceriesContract.Groceries.COLUMN_NAME_BUY_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_CREATED_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_MODIFIED_DATE,
                TypesContract.Types.COLUMN_NAME_TYPES_NAME
        };

        String query = String.format("SELECT %s FROM %s JOIN %s ON %s = %s ORDER BY %s ASC",
                TextUtils.join(", ", projections),
                GroceriesContract.Groceries.TABLE_NAME,
                TypesContract.Types.TABLE_NAME,
                TypesContract.Types.TABLE_NAME + "." + TypesContract.Types._ID,
                GroceriesContract.Groceries.TABLE_NAME + "."
                        + GroceriesContract.Groceries.COLUMN_NAME_TYPE_ID,
                GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE);

        if (typeId != null) {
            query = String.format("SELECT %s FROM %s JOIN %s ON %s = %s "
                    + "WHERE %s = %o ORDER BY %s ASC",
                    TextUtils.join(", ", projections),
                    GroceriesContract.Groceries.TABLE_NAME,
                    TypesContract.Types.TABLE_NAME,
                    TypesContract.Types.TABLE_NAME + "." + TypesContract.Types._ID,
                    GroceriesContract.Groceries.TABLE_NAME + "."
                            + GroceriesContract.Groceries.COLUMN_NAME_TYPE_ID,
                    GroceriesContract.Groceries.COLUMN_NAME_TYPE_ID,
                    typeId,
                    GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE);
        }

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
        values.put(GroceriesContract.Groceries.COLUMN_NAME_TYPE_ID, groceriesItem.type());

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
                GroceriesContract.Groceries.TABLE_NAME + "." + GroceriesContract.Groceries._ID,
                GroceriesContract.Groceries.COLUMN_NAME_NAME,
                GroceriesContract.Groceries.COLUMN_NAME_BUY_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_EXPIRE_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_CREATED_DATE,
                GroceriesContract.Groceries.COLUMN_NAME_MODIFIED_DATE,
                TypesContract.Types.COLUMN_NAME_TYPES_NAME,
        };

        String query = String.format("SELECT %s FROM %s JOIN %s ON %s = %s WHERE %s = %o",
                TextUtils.join(", ", projections),
                GroceriesContract.Groceries.TABLE_NAME,
                TypesContract.Types.TABLE_NAME,
                TypesContract.Types.TABLE_NAME + "." + TypesContract.Types._ID,
                GroceriesContract.Groceries.TABLE_NAME + "."
                        + GroceriesContract.Groceries.COLUMN_NAME_TYPE_ID,
                GroceriesContract.Groceries.TABLE_NAME + "." + GroceriesContract.Groceries._ID,
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
        return getGroceries(null)
                .map(this::removeUnexpiredItems);
    }

    private List<GroceriesItem> removeUnexpiredItems(List<GroceriesItem> groceriesItems) {
        for (Iterator<GroceriesItem> iterator = groceriesItems.iterator(); iterator.hasNext(); ) {
            if (DateUtils.dayDiff(iterator.next().expiredDate(),
                    (System.currentTimeMillis() / 1000L)) > 10) {
                iterator.remove();
            }
        }
        return groceriesItems;
    }

    @Override
    public Observable<List<TypesItem>> getTypes() {
        String[] projections = {
                TypesContract.Types._ID,
                TypesContract.Types.COLUMN_NAME_TYPES_NAME
        };

        String query = String.format("SELECT %s FROM %s",
                TextUtils.join(", ", projections),
                TypesContract.Types.TABLE_NAME);

        return databaseHelper
                .createQuery(TypesContract.Types.TABLE_NAME, query)
                .mapToList(taskMapperCursorToListTypes);
    }

    @Override
    public Observable<Dashboards> getDashboadInfo() {
        return Observable.zip(getGroceries(null), getAlmostExpiredGroceriesList(), getTypes(),
                (items, itemsThatExpired, typesItems) ->
                        Dashboards.create(
                                items.size(),
                                itemsThatExpired.size(),
                                typesItems));
    }

    @Override
    public Observable<TypesItem> getTypeInfo(long id) {
        String[] projections = {
                TypesContract.Types._ID,
                TypesContract.Types.COLUMN_NAME_TYPES_NAME
        };

        String query = String.format("SELECT %s FROM %s WHERE %s = %o",
                TextUtils.join(", ", projections),
                TypesContract.Types.TABLE_NAME,
                TypesContract.Types._ID,
                id);

        return databaseHelper
                .createQuery(TypesContract.Types.TABLE_NAME, query)
                .mapToOne(taskMapperCursorToListTypes);
    }

}
