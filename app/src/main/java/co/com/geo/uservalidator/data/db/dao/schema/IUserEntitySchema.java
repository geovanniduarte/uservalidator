package co.com.geo.uservalidator.data.db.dao.schema;

public interface IUserEntitySchema {
    String USER_TABLE = "users";
    String COLUMN_ID = "_id";
    String COLUMN_USER_NAME = "user_name";
    String COLUMN_PASSWORD = "password";
    String USER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + USER_TABLE
            + " ("
            + COLUMN_ID
            + " INTEGER PRIMARY KEY autoincrement, "
            + COLUMN_USER_NAME
            + " TEXT NOT NULL,"
            + COLUMN_PASSWORD
            + " TEXT NOT NULL"
            + ")";

    String[] USER_COLUMNS = new String[] { COLUMN_ID, COLUMN_USER_NAME, COLUMN_PASSWORD};
}
