package com.example.uy.notificationex.service;

public class MyFirebaseInstanceIDService {
    /*
     public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements OnResponseListener {
    private final String TAG = this.getClass().getSimpleName();

    private AuthPresenter mAuthPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthPresenter = new AuthPresenter(this, this);
    }

    @Override
    public void onTokenRefresh() {
        try {
            String tokenRefresh = FirebaseInstanceId.getInstance().getToken();
            if (mAuthPresenter != null && !BaseApplication.getInstance().sPrefManager.getAccessToken().equals("")) {
                mAuthPresenter.addDevice(MobijuceHelper.getAndroidOsVersion(), tokenRefresh, Constant.DEVICE_ANDROID);
            }
        } catch (Exception e) {
            MobijuceHelper.logExceptionCrashlytics(e);
        }
    }

    @Override
    public boolean onResponse(ApiTask task, int status, String messageError) {
        return false;
    }

    @Override
    public boolean willProcess(ApiTask task, int status) {
        return false;
    }
}
    */
}
