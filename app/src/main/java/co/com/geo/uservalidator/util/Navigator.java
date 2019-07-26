package co.com.geo.uservalidator.util;

import android.app.Activity;
import android.content.Intent;
import co.com.geo.uservalidator.presentation.detail.UserDetailActivity;


public class Navigator {
    public void openUserDetail(Activity activity, String username, int requestCode) {
        Intent intent = UserDetailActivity.intent(activity, username);
        activity.startActivityForResult(intent, requestCode);
    }
}