package co.com.geo.uservalidator.di.modules;

import android.app.Application;
import android.content.Context;

import co.com.geo.uservalidator.util.Constants;
import co.com.geo.uservalidator.util.GPSLocation;
import co.com.geo.uservalidator.util.Navigator;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application mApp;

    public ApplicationModule(Application app) {
        this.mApp = app;
    }

    @Provides
    public Context provideAppContext() {
        return mApp.getApplicationContext();
    }

    @Provides
    public Navigator provideNavigator() {
        return new Navigator();
    }

    @Provides
    public GPSLocation provideGpsLocation() {
        return new GPSLocation(Constants.MIN_RESTRICTION_DISTANCE);
    }
}
