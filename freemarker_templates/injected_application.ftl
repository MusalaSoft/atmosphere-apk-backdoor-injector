package ${injected_application_package};

import ${package}.${application_name};
import android.os.Build;
import android.webkit.WebView;

public class ${injected_application_name} extends ${application_name} {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }
}
