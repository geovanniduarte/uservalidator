package co.com.geo.uservalidator.data.db.dao.mapper;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.com.geo.uservalidator.data.db.dao.schema.IIntentEntitySchema;
import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.data.model.IntentEntity;

public class CursorIntentEntityMapper implements CursorMapper<Cursor, IntentEntity>, IIntentEntitySchema {

    private IntentEntity transform(Cursor input) {
        IntentEntity intent = new IntentEntity();

        int userNameIndex;
        int dateIndex;
        int resultIndex;
        int latitudeIndex;
        int longitudeIndex;



        if (input != null) {
            if (input.getColumnIndex(COLUMN_INTENT_USER_NAME) != -1) {
                userNameIndex = input.getColumnIndexOrThrow(COLUMN_INTENT_USER_NAME);
                intent.setUsername(input.getString(userNameIndex));
            }
            if (input.getColumnIndex(COLUMN_INTENT_DATE) != -1) {
                dateIndex = input.getColumnIndexOrThrow(
                        COLUMN_INTENT_DATE);
                intent.setDate(input.getString(dateIndex));
            }
            if (input.getColumnIndex(COLUMN_INTENT_RESULT) != -1) {
                resultIndex = input.getColumnIndexOrThrow(COLUMN_INTENT_RESULT);
                intent.setResult(input.getInt(resultIndex) == 1);
            }
            if (input.getColumnIndex(COLUMN_INTENT_LATITUDE) != -1) {
                latitudeIndex = input.getColumnIndexOrThrow(COLUMN_INTENT_LATITUDE);
                intent.setLatitude(input.getString(latitudeIndex));
            }
            if (input.getColumnIndex(COLUMN_INTENT_LATITUDE) != -1) {
                longitudeIndex = input.getColumnIndexOrThrow(COLUMN_INTENT_LATITUDE);
                intent.setLongitude(input.getString(longitudeIndex));
            }
        }
        return intent;
    }

    @Override
    public List<IntentEntity> transformList(Cursor inputListCursor) {
        List<IntentEntity> intentList = new ArrayList<IntentEntity>();
        if (inputListCursor != null) {
            inputListCursor.moveToFirst();
            while (!inputListCursor.isAfterLast()) {
                IntentEntity user = transform(inputListCursor);
                intentList.add(user);
                inputListCursor.moveToNext();
            }
            inputListCursor.close();
        }
        return intentList;
    }
}
