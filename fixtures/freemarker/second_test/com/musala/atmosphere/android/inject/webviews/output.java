package com.musala.atmosphere.android.inject.webviews;

import com.real.application.RealApplication;
import android.os.Build;
import android.webkit.WebView;

public class InjectedWebViewApplication extends RealApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }
}
