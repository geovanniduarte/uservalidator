package co.com.geo.uservalidator.data.repository.datasource;

import android.location.Location;

import org.json.JSONObject;

import co.com.geo.uservalidator.data.model.GeoName;
import co.com.geo.uservalidator.data.model.IntentEntity;
import co.com.geo.uservalidator.data.net.SystemService;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;

public class ApiDataSource {

    private SystemService mSystemService;

    public ApiDataSource(SystemService systemService) {
        this.mSystemService = systemService;
    }

    public Flowable<GeoName> getDateFromLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String url = "http://api.geonames.org/timezoneJSON?formatted=true&lat=%s&lng=%s&username=qa_mobile_easy&style=full";
        final String URL = String.format(url, String.valueOf(latitude), String.valueOf(longitude));

        return Flowable.create(new FlowableOnSubscribe<JSONObject>() {
            @Override
            public void subscribe(FlowableEmitter<JSONObject> emitter) throws Exception {
                JSONObject jsonObject = mSystemService.getDateFromLocation(URL);
                emitter.onNext(jsonObject);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
               .map(new Function<JSONObject, GeoName>() {
                   @Override
                   public GeoName apply(JSONObject jsonObject) throws Exception {
                        String time = jsonObject.getString("time");
                        String country = jsonObject.getString("countryName");
                        double latitude = jsonObject.getDouble("lat");
                        double longitude = jsonObject.getDouble("lng");
                        return new GeoName(time, country, latitude, longitude);
                   }
               });
    }
}
