package co.com.geo.uservalidator.di.modules;

import android.content.Context;

import co.com.geo.uservalidator.data.db.dao.mapper.CursorIntentEntityMapper;
import co.com.geo.uservalidator.data.net.SystemService;
import co.com.geo.uservalidator.data.repository.datasource.ApiDataSource;
import co.com.geo.uservalidator.util.GPSLocation;
import co.com.geo.uservalidator.data.db.dao.UserEntityDao;
import co.com.geo.uservalidator.data.db.dao.mapper.CursorUserEntityMapper;
import co.com.geo.uservalidator.data.db.db.Database;
import co.com.geo.uservalidator.data.repository.UserRepository;
import co.com.geo.uservalidator.data.repository.datasource.DeviceDataSource;
import co.com.geo.uservalidator.data.repository.datasource.LocalDatasource;
import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    @Provides
    public CursorUserEntityMapper provideCursorUserEntityMapper() {
        return new CursorUserEntityMapper();
    }

    @Provides
    public Database provideDatabase(Context context) {
        Database myDatabase = new Database(context);
        return myDatabase;
    }

    @Provides
    public UserEntityDao provideUserEntityDao(Database database, CursorUserEntityMapper cursorEntityMapper, CursorIntentEntityMapper cursorIntentEntityMapper) {
        database.open();
        return new UserEntityDao(database.getWritableDatabase(), cursorEntityMapper, cursorIntentEntityMapper);
    }

    @Provides
    public LocalDatasource provideLocalDataSource(UserEntityDao userEntityDao) {
        return new LocalDatasource(userEntityDao);
    }

    @Provides
    public UserRepository provideUserRepository(LocalDatasource localDatasource, DeviceDataSource deviceDataSource, ApiDataSource apiDataSource) {
        return new UserRepository(localDatasource, deviceDataSource, apiDataSource);
    }

    @Provides
    public DeviceDataSource provideDeviceDataSoruce(Context context, GPSLocation gpsLocation) {
        return new DeviceDataSource(context, gpsLocation);
    }

    @Provides
    public SystemService provideSystemService() {
        return new SystemService();
    }

    @Provides
    public ApiDataSource provideApiDataSource(SystemService systemService) {
        return new ApiDataSource(systemService);
    }

    @Provides
    public CursorIntentEntityMapper provideCursorIntentEntityMapper() {
        return new CursorIntentEntityMapper();
    }
}
