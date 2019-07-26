package co.com.geo.uservalidator.data.repository.datasource;

import java.util.List;
import java.util.concurrent.Callable;

import co.com.geo.uservalidator.data.db.dao.UserEntityDao;
import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.data.model.UserEntity;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;

public class LocalDatasource implements UserDatasource{

    private UserEntityDao userEntityDao;

    public LocalDatasource(UserEntityDao userEntityDao) {
        this.userEntityDao = userEntityDao;
    }


    @Override
    public Flowable<Boolean> insertUserEntity(final UserEntity userEntity) {
       return Flowable.fromCallable(
               new Callable<Boolean>() {
                   @Override
                   public Boolean call() throws Exception {
                       return userEntityDao.addUserEntity(userEntity);
                   }
               }
        );
    }

    public Flowable<Boolean> insertIntentEntity(final IntentEntity intentEntity) {
        return Flowable.fromCallable(
                new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return userEntityDao.addIntentEntity(intentEntity);
                    }
                }
        );
    }


    @Override
    public Flowable<List<IntentEntity>> getIntentsByUserName(final String username) {
        return Flowable.fromCallable(new Callable<List<IntentEntity>>() {
            @Override
            public List<IntentEntity> call() throws Exception {
                return userEntityDao.fetchIntentsBy(username);
            }
        });
    }

    @Override
    public Flowable<UserEntity> getUserEntityByUsername(final String username) {
        return Flowable.create(new FlowableOnSubscribe<UserEntity>() {
            @Override
            public void subscribe(FlowableEmitter<UserEntity> emitter) throws Exception {
                UserEntity userEntity = userEntityDao.fetchUserEntityByUsername(username);
                emitter.onNext(userEntity);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }
}
