package co.com.geo.uservalidator.data.db.dao.mapper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.com.geo.uservalidator.data.db.dao.schema.IUserEntitySchema;
import co.com.geo.uservalidator.data.model.UserEntity;

public class CursorUserEntityMapper implements CursorMapper<Cursor, UserEntity>, IUserEntitySchema {

    private UserEntity transform(Cursor input) {
        UserEntity user = new UserEntity();

        int idIndex;
        int userNameIndex;
        int passwordIndex;


        if (input != null) {
            if (input.getColumnIndex(COLUMN_ID) != -1) {
                idIndex = input.getColumnIndexOrThrow(COLUMN_ID);
                user.setId(input.getLong(idIndex));
            }
            if (input.getColumnIndex(COLUMN_USER_NAME) != -1) {
                userNameIndex = input.getColumnIndexOrThrow(
                        COLUMN_USER_NAME);
                user.setUsername(input.getString(userNameIndex));
            }
            if (input.getColumnIndex(COLUMN_PASSWORD) != -1) {
                passwordIndex = input.getColumnIndexOrThrow(COLUMN_PASSWORD);
                user.setPassword(input.getString(passwordIndex));
            }
        }
        return user;
    }

    @Override
    public List<UserEntity> transformList(Cursor inputListCursor) {
        List<UserEntity> userList = new ArrayList<UserEntity>();
        if (inputListCursor != null) {
            inputListCursor.moveToFirst();
            while (!inputListCursor.isAfterLast()) {
                UserEntity user = transform(inputListCursor);
                userList.add(user);
                inputListCursor.moveToNext();
            }
            inputListCursor.close();
        }
        return userList;
    }
}
