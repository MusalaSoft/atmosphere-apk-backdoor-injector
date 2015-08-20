package com.musala.atmosphere.android.inject.webviews;

import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

public class InjectedWebViewApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }
}
