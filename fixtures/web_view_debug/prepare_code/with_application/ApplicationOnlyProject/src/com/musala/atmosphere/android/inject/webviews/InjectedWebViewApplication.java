package com.musala.atmosphere.android.inject.webviews;

import com.other.example.StudyApplication;
import android.os.Build;
import android.webkit.WebView;

public class InjectedWebViewApplication extends StudyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }
}
