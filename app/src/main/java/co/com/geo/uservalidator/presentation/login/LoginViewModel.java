package co.com.geo.uservalidator.presentation.login;

import android.arch.lifecycle.MutableLiveData;
import android.location.Location;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import javax.inject.Inject;

import co.com.geo.uservalidator.data.model.GeoName;
import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.util.mvvm.BaseViewModel;
import co.com.geo.uservalidator.data.model.UserEntity;
import co.com.geo.uservalidator.data.repository.UserRepository;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends BaseViewModel {

    private UserRepository userRepository;

    public MutableLiveData<Boolean> loadingQueryState = new MutableLiveData();
    public MutableLiveData<UserEntity> userEntityQueryState = new MutableLiveData();

    public MutableLiveData<Boolean> userSaveState = new MutableLiveData();
    public MutableLiveData<Boolean> loadingSavingState = new MutableLiveData();

    public MutableLiveData<Boolean> loadingLocationGeoNameIntentState = new MutableLiveData();
    public MutableLiveData<Boolean> LocationGeoNameIntentSavedState = new MutableLiveData();

    @Inject
    public LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void insertUserEntity(UserEntity userEntity) {
        Disposable disposable = this.userRepository.insertUserEntity(userEntity)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        loadingSavingState.postValue(false);
                    }
                })
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        loadingSavingState.postValue(true);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Boolean>() { //next
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        userSaveState.postValue(aBoolean);
                    }
                }, new Consumer<Throwable>() { //error
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });

        compositeDisposable.add(disposable);
    }

    public void queryUserEntity(String username) {
       Disposable disposable =  this.userRepository.getUserByUsername(username)
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        loadingQueryState.postValue(false);
                    }
                })
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        loadingQueryState.postValue(true);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<UserEntity>() { //next
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                        userEntityQueryState.setValue(userEntity);
                    }
                }, new Consumer<Throwable>() { //error
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        userEntityQueryState.setValue(null);
                    }
                });

       compositeDisposable.add(disposable);
    }

    public void beginIntentProcess(final boolean isSuccess, final UserEntity userToVerify) {
        Disposable disposable = this.userRepository.requestLocation() //getLocation
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        loadingLocationGeoNameIntentState.postValue(false);
                    }
                })
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        loadingLocationGeoNameIntentState.postValue(true);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                })
                .flatMap(new Function<Location, Publisher<GeoName>>() {
                    @Override
                    public Publisher<GeoName> apply(Location location) throws Exception {
                        return queryGeoNameToLocation(location);
                    }
                })
                .flatMap(new Function<GeoName, Publisher<Boolean>>() {
                    @Override
                    public Publisher<Boolean> apply(GeoName geoName) throws Exception {
                        return saveIntentEntity(geoName, userToVerify, isSuccess);
                    }
                })
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        LocationGeoNameIntentSavedState.setValue(aBoolean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Disposable disposable1 = saveIntentEntity(new GeoName(), userToVerify, isSuccess)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(new Consumer<Subscription>() {
                                    @Override
                                    public void accept(Subscription subscription) throws Exception {
                                        loadingLocationGeoNameIntentState.postValue(true);
                                    }
                                })
                                .doOnTerminate(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        loadingLocationGeoNameIntentState.postValue(false);
                                    }
                                })
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        LocationGeoNameIntentSavedState.setValue(aBoolean);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        throwable.printStackTrace();
                                    }
                                });

                        compositeDisposable.add(disposable1);
                    }
                });


        compositeDisposable.add(disposable);
    }

    public Flowable<GeoName> queryGeoNameToLocation(final Location location) {
        return userRepository.getGeoNameFromLocation(location)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());

    }

    public Flowable<Boolean> saveIntentEntity(GeoName geoName, UserEntity userToVerify, boolean isSuccess) {
        IntentEntity intentEntity = new IntentEntity(userToVerify.getUsername(), geoName.getTime(), isSuccess, String.valueOf(geoName.getLatitude()), String.valueOf( geoName.getLongitude()));
        return userRepository.saveIntentEntity(intentEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

}
