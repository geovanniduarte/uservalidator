package co.com.geo.uservalidator;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import co.com.geo.uservalidator.data.db.dao.UserEntityDao;
import co.com.geo.uservalidator.data.db.dao.mapper.CursorIntentEntityMapper;
import co.com.geo.uservalidator.data.db.dao.mapper.CursorUserEntityMapper;
import co.com.geo.uservalidator.data.db.db.Database;
import co.com.geo.uservalidator.data.model.GeoName;
import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.data.model.UserEntity;
import co.com.geo.uservalidator.data.net.SystemService;
import co.com.geo.uservalidator.data.repository.datasource.ApiDataSource;
import io.reactivex.functions.Predicate;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Context appContext;
    private Database testDatabase;
    private SQLiteDatabase testDB;
    private CursorUserEntityMapper testCursorEntityMapper;
    private CursorIntentEntityMapper testCursorIntentMapper;
    private UserEntityDao testUserEntityDao;
    private ApiDataSource apiDataSource;
    private SystemService systemService;


    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getTargetContext();

        testDatabase = new Database(appContext);
        testDatabase.open();

        testDB = testDatabase.getWritableDatabase();
        testCursorEntityMapper = new CursorUserEntityMapper();
        testCursorIntentMapper = new CursorIntentEntityMapper();
        testUserEntityDao = new UserEntityDao(testDB, testCursorEntityMapper, testCursorIntentMapper);

        systemService = new SystemService();
        apiDataSource = new ApiDataSource(systemService);
    }

    @Test
    public void transform_cursor_to_userentity() {

        final Cursor cursor = new MatrixCursor(new String[]{"id", "user_name", "password"});
        ((MatrixCursor) cursor).addRow(new Object[]{1, "Geo", "Geo1"});

        List<UserEntity> testUsers = testCursorEntityMapper.transformList(cursor);
        UserEntity userEntity = testUsers.get(0);

        assertEquals(userEntity.getUsername(), "Geo");
        assertEquals(userEntity.getPassword(), "Geodd");

    }


    @Test
    public void insert_userentity_and_fetch_before() {
        UserEntity testUserEntity = new UserEntity("Geo", "Geo2");
        boolean testRta = testUserEntityDao.addUserEntity(testUserEntity);
        assertEquals(testRta, true);

        int testUsersSize = testUserEntityDao.fetchAllUsersEntity().size();
        assertEquals(testUsersSize > 0, true);

        UserEntity testFetchedUserEntity = testUserEntityDao.fetchUserEntityById(0);
        assertEquals(testFetchedUserEntity.getId(), 0 );
        assertEquals(testFetchedUserEntity.getUsername(), "Geo");
    }

    @Test
    public void get_geoname_from_server() {
        Location location = new Location("gps");
        location.setLatitude(4.7190);
        location.setLongitude(-74.07);
        apiDataSource.getDateFromLocation(location)
                .test()
                .assertValue(new Predicate<GeoName>() {
                    @Override
                    public boolean test(GeoName geoName) throws Exception {
                       return geoName.getCountryName().equals("Colombia");
                    }
                })
                .assertValue(new Predicate<GeoName>() {
                    @Override
                    public boolean test(GeoName geoName) throws Exception {
                        return geoName.getTime().equals("2019-07-25 19:56");
                    }
                })
               .assertValue(new Predicate<GeoName>() {
                    @Override
                    public boolean test(GeoName geoName) throws Exception {
                        return geoName.getLatitude() == 4.7190;
                    }
                });
    }

    @Test
    public void test_intents_by_username() {
        List<IntentEntity> intents = testUserEntityDao.fetchIntentsBy("hggghhgghj@rtyuii.po");
        assertEquals(intents.size() > 0, true);

        IntentEntity testFetchedUserEntity = intents.get(0);

        assertEquals(testFetchedUserEntity.getUsername(), "hggghhgghj@rtyuii.po" );
    }

}
