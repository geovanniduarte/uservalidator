package co.com.geo.uservalidator.data.db.dao.schema;

public interface IIntentEntitySchema {
    String INTENT_TABLE = "intent";
    String COLUMN_INTENT_USER_NAME = "user_name";
    String COLUMN_INTENT_DATE = "date";
    String COLUMN_INTENT_RESULT = "result";
    String COLUMN_INTENT_LATITUDE = "latitude";
    String COLUMN_INTENT_LONGITUDE = "longitude";
    String INTENT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + INTENT_TABLE
            + " ("
            + COLUMN_INTENT_USER_NAME
            + " TEXT NOT NULL, "
            + COLUMN_INTENT_DATE
            + " TEXT,"
            + COLUMN_INTENT_RESULT
            + " INTEGER,"
            + COLUMN_INTENT_LATITUDE
            + " TEXT NOT NULL,"
            + COLUMN_INTENT_LONGITUDE
            + " TEXT NOT NULL"
            + ")";

    String[] INTENT_COLUMNS = new String[] { COLUMN_INTENT_USER_NAME, COLUMN_INTENT_DATE, COLUMN_INTENT_RESULT, COLUMN_INTENT_LATITUDE, COLUMN_INTENT_LONGITUDE};
}
