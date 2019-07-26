package co.com.geo.uservalidator.data.repository;

import android.content.Intent;
import android.location.Location;

import java.util.List;

import co.com.geo.uservalidator.data.model.GeoName;
import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.data.model.UserEntity;
import co.com.geo.uservalidator.data.repository.datasource.ApiDataSource;
import co.com.geo.uservalidator.data.repository.datasource.DeviceDataSource;
import co.com.geo.uservalidator.data.repository.datasource.LocalDatasource;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class UserRepository {

    private LocalDatasource localDatasource;
    private DeviceDataSource deviceDataSource;
    private ApiDataSource apiDataSource;

    public UserRepository(LocalDatasource localDatasource, DeviceDataSource deviceDataSource, ApiDataSource apiDataSource) {
        this.localDatasource = localDatasource;
        this.deviceDataSource = deviceDataSource;
        this.apiDataSource = apiDataSource;
    }

    public Flowable<Boolean> insertUserEntity(UserEntity userEntity) {
        return this.localDatasource.insertUserEntity(userEntity);
    }

    public Flowable<UserEntity> getUserByUsername(String username) {
        return this.localDatasource.getUserEntityByUsername(username);
    }

    public Flowable<Location> requestLocation() {
        return this.deviceDataSource.requestLocation();
    }

    public Flowable<GeoName> getGeoNameFromLocation(Location location) {
        return this.apiDataSource.getDateFromLocation(location);
    }

    public Flowable<Boolean> saveIntentEntity(IntentEntity intentEntity) {
        return this.localDatasource.insertIntentEntity(intentEntity);
    }

    public Flowable<List<IntentEntity>> getIntentsBy(String username) {
        return this.localDatasource.getIntentsByUserName(username);
    }
}
