package co.com.geo.uservalidator.data.repository.datasource;

import android.content.Context;
import android.location.Location;

import co.com.geo.uservalidator.util.GPSLocation;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class DeviceDataSource {

    private Context mContext;
    private GPSLocation mGpsLocation;

    public DeviceDataSource(Context context, GPSLocation gpsLocation){
        this.mContext = context;
        this.mGpsLocation = gpsLocation;
    }

    public Flowable<Location> requestLocation() {
       return Flowable.create(new FlowableOnSubscribe<Location>() {
            @Override
            public void subscribe(final FlowableEmitter<Location> emitter) throws Exception {
                GPSLocation.LocationResult locationResult = new GPSLocation.LocationResult() {
                    @Override
                    public void gotLocation(Location location, boolean isNew) {
                        if (location == null) {
                            location = new Location("gps");
                        }
                        emitter.onNext(location);
                        emitter.onComplete();
                    }
                };
                mGpsLocation.getLocation(mContext, locationResult);
            }
        }, BackpressureStrategy.BUFFER);
    }

}
