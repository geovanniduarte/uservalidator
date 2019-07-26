package co.com.geo.uservalidator.presentation;

import android.app.Application;

import com.facebook.stetho.Stetho;

import co.com.geo.uservalidator.di.components.ApplicationComponent;
import co.com.geo.uservalidator.di.components.DaggerApplicationComponent;
import co.com.geo.uservalidator.di.modules.ApplicationModule;

public class ValidatorApp extends Application {

    public ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
