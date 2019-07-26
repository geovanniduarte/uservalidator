package co.com.geo.uservalidator.util.mvvm;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import co.com.geo.uservalidator.presentation.detail.UserDetailViewModel;
import co.com.geo.uservalidator.presentation.login.LoginViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    protected abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    protected abstract ViewModel postListViewModel(LoginViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailViewModel.class)
    protected abstract ViewModel postUserDetailViewModel(UserDetailViewModel viewModel);
}