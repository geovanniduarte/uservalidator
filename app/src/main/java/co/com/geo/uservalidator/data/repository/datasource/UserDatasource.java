package co.com.geo.uservalidator.data.repository.datasource;

import android.support.design.widget.FloatingActionButton;

import java.util.List;

import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.data.model.UserEntity;
import io.reactivex.Flowable;

public interface UserDatasource {
    Flowable<Boolean> insertUserEntity(UserEntity userEntity);
    Flowable<List<IntentEntity>> getIntentsByUserName(String username);
    Flowable<UserEntity> getUserEntityByUsername(String username);
}
