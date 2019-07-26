package co.com.geo.uservalidator.data.db.dao.mapper;

import android.database.Cursor;

import java.util.List;

public interface CursorMapper<R extends Cursor, T> {
    //public T transform(R input);
    public List<T> transformList(R inputListCursor);
}
