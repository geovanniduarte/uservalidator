package co.com.geo.uservalidator.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import co.com.geo.uservalidator.data.db.dao.mapper.CursorIntentEntityMapper;
import co.com.geo.uservalidator.data.db.dao.mapper.CursorUserEntityMapper;
import co.com.geo.uservalidator.data.db.dao.schema.IIntentEntitySchema;
import co.com.geo.uservalidator.data.db.dao.schema.IUserEntitySchema;
import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.data.model.UserEntity;

public class UserEntityDao extends DbContentProvider
        implements IUserEntitySchema, IIntentEntitySchema, IUserEntityDao {

    private Cursor cursor;
    private ContentValues initialUserValues;
    private ContentValues initialIntentValues;
    private CursorUserEntityMapper cursorUserEntityMapper;
    private CursorIntentEntityMapper cursorIntentEntityMapper;

    public UserEntityDao(SQLiteDatabase db, CursorUserEntityMapper cursorUserEntityMapper, CursorIntentEntityMapper cursorIntentEntityMapper) {
        super(db);
        this.cursorUserEntityMapper = cursorUserEntityMapper;
        this.cursorIntentEntityMapper = cursorIntentEntityMapper;
    }

    public UserEntity fetchUserEntityById(int id) {
        final String selectionArgs[] = { String.valueOf(id) };
        final String selection = COLUMN_ID + " = ?";
        cursor = super.query(USER_TABLE, USER_COLUMNS, selection,
                selectionArgs, COLUMN_ID);
        UserEntity user = null;
        List<UserEntity> userList = this.cursorUserEntityMapper.transformList(cursor);
        if (userList.size() > 0) {
            user = userList.get(0);
        }
        return user;
    }

    public UserEntity fetchUserEntityByUsername(String username) {
        final String selectionArgs[] = { String.valueOf(username) };
        final String selection = COLUMN_USER_NAME + " = ?";
        cursor = super.query(USER_TABLE, USER_COLUMNS, selection,
                selectionArgs, COLUMN_ID);
        UserEntity user = null;
        List<UserEntity> userList = this.cursorUserEntityMapper.transformList(cursor);
        if (userList.size() > 0) {
            user = userList.get(0);
        }
        return user;
    }

    public List<UserEntity> fetchAllUsersEntity() {
        cursor = super.query(USER_TABLE, USER_COLUMNS, null,
                null, COLUMN_ID);

        List<UserEntity> userList = this.cursorUserEntityMapper.transformList(cursor);
        return userList;
    }

    public List<IntentEntity> fetchIntentsBy(String username) {
        final String selectionArgs[] = { String.valueOf(username) };
        final String selection = COLUMN_INTENT_USER_NAME + " = ?";
        cursor = super.query(INTENT_TABLE, INTENT_COLUMNS, selection,
                selectionArgs, COLUMN_INTENT_USER_NAME);
        IntentEntity intent = null;
        List<IntentEntity> intentList = this.cursorIntentEntityMapper.transformList(cursor);

        return intentList;
    }

    public boolean addUserEntity(UserEntity user) {
        // set values
        setUserContentValue(user);
        try {
            return super.insert(USER_TABLE, getUserContentValue()) > 0;
        } catch (SQLiteConstraintException ex){
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    public boolean addIntentEntity(IntentEntity intentEntity) {
        // set values
        setIntentContentValue(intentEntity);
        try {
            return super.insert(INTENT_TABLE, getIntentContentValue()) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    public boolean deleteAllUsersEntity() {
        return false;
    }

    private void setUserContentValue(UserEntity user) {
        initialUserValues = new ContentValues();
        initialUserValues.put(COLUMN_USER_NAME, user.getUsername());
        initialUserValues.put(COLUMN_PASSWORD, user.getPassword());

    }



    private ContentValues getUserContentValue() {
        return initialUserValues;
    }

    private void setIntentContentValue(IntentEntity intent) {
        initialIntentValues = new ContentValues();
        initialIntentValues.put(COLUMN_INTENT_USER_NAME, intent.getUsername());
        initialIntentValues.put(COLUMN_INTENT_DATE, intent.getDate());
        initialIntentValues.put(COLUMN_INTENT_RESULT, intent.isResult());
        initialIntentValues.put(COLUMN_INTENT_LATITUDE, intent.getLatitude());
        initialIntentValues.put(COLUMN_INTENT_LONGITUDE, intent.getLongitude());
    }

    private ContentValues getIntentContentValue() {
        return initialIntentValues;
    }

}
