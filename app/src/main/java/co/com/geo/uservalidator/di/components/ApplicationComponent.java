package co.com.geo.uservalidator.di.components;

import co.com.geo.uservalidator.presentation.detail.UserDetailActivity;
import co.com.geo.uservalidator.util.mvvm.ViewModelModule;
import co.com.geo.uservalidator.di.modules.ApplicationModule;
import co.com.geo.uservalidator.di.modules.DataModule;
import co.com.geo.uservalidator.presentation.login.LoginFragment;
import dagger.Component;

@Component(modules = {ApplicationModule.class, DataModule.class, ViewModelModule.class})
public interface ApplicationComponent {
    void inject(LoginFragment loginFragment);
    void inject(UserDetailActivity userDetailActivity);
}
