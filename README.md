# atmosphere-apk-backdoor-injector
The Android OS requires WebView debugging to be manually enabled in every Android app whose WebViews we would like to be able to debug by calling the static method [`setWebContentsDebuggingEnabled`][1] like this:
``` java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    WebView.setWebContentsDebuggingEnabled(true);
}
```
[1]: https://developer.android.com/reference/android/webkit/WebView.html#setWebContentsDebuggingEnabled(boolean)

Usually the applications in production have WebView debugging disabled.

The Atmosphere APK Backdoor Injector is a tool that helps you enable WebView debugging by injecting the missing code in the APK file you provide.

## Project setup

### Requirements
* Android SDK - [setup instructions](https://github.com/MusalaSoft/atmosphere-docs/blob/master/setup/android_sdk.md)
* Ant - [setup instructions](https://github.com/MusalaSoft/atmosphere-docs/blob/master/setup/ant.md)
* Java Development Kit (JDK) - [setup instructions](https://github.com/MusalaSoft/atmosphere-docs/blob/master/setup/jdk.md)

### Paths setup
Create a `local.properties` file in the project root directory and add the following properties:
```
android.executable.path=
jarsigner.path=
ant.executable.path=
android.build.tools.path=
```

**android.executable.path** should point to the `android` executable file in the Android SDK directory. It is usually located in `[android-sdk]/tools/`. **Note:** If there are whitespaces in the path, surround the whole path with quotes. Also, if you are on Windows, include the `.bat` extension. Example: `"C:\\android-sdk\\tools\\android.bat"`

**jarsigner.path** should be the path to the jarsigner executable, part of the JDK. It is located in the `[jdk-directory]/bin/` **Note:** If there are whitespaces in the path, surround the whole path with quotes. For Windows the default path would be: `"C:\\Program Files\\Java\\jdk[version]\\bin\\jarsigner.exe"`

**ant.executable.path** should point to the Ant executable, located in `[ant-directory]/bin/`. If you use Windows, include the `.bat` extension. Example: `C:\\apache-ant-1.9.7\\bin\\ant.bat`

**android.build.tools.path** should be the path to the directory of a version of the Android SDK build-tools. If there are whitespaces in the path, **quotes are not needed**. Example on Windows: `C:\\android-sdk\\build-tools\\23.0.3`

## Usage
The Atmosphere APK Backdoor Injector uses the JUnit tests in `src/test` to inject APK files, there is no executable. You can copy you APK file to `fixtures/apk_backdoor` and rename it to `SimpleWebViewApplication.apk`, replacing the original. Then run `./gradlew build` (Linux/macOS) or `gradlew build` (Windows) in the project root directory. After the project builds successfully, the injected APK file should be available in `fixtures/apk_backdoor` with a suffix `-debuggable` appended to the file name.
