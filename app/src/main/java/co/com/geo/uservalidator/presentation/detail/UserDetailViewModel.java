package co.com.geo.uservalidator.presentation.detail;

import android.arch.lifecycle.MutableLiveData;

import org.reactivestreams.Subscription;

import java.util.List;

import javax.inject.Inject;

import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.data.repository.UserRepository;
import co.com.geo.uservalidator.util.mvvm.BaseViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UserDetailViewModel extends BaseViewModel {

    private UserRepository userRepository;

    public MutableLiveData<Boolean> loadingIntentsState = new MutableLiveData();
    public MutableLiveData<List<IntentEntity>> intentsState = new MutableLiveData();

    @Inject
    public UserDetailViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void loadIntents(String username) {
        Disposable disposable =  this.userRepository.getIntentsBy(username)
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        loadingIntentsState.postValue(false);
                    }
                })
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        loadingIntentsState.postValue(true);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<IntentEntity>>() {
                    @Override
                    public void accept(List<IntentEntity> intentEntities) throws Exception {
                        intentsState.setValue(intentEntities);
                    }
                });

        compositeDisposable.add(disposable);


    }

}
