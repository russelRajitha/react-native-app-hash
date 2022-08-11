package com.reactnativeapphash;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.facebook.react.bridge.Promise;


public class AppHashModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext context;

    AppHashModule(ReactApplicationContext context) {
        super(context);
        this.context = context;
    }

    @NonNull
    @Override
    public String getName() {
        return "AppHashModule";
    }

    @ReactMethod
    public void getAppHash(Promise promise) {
        String hash = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("AppHashModule", "hashKey : " + hashKey);
                if (hashKey.length() > 0){
                    hash = hashKey;
                }
            }
            promise.resolve(hash);
        } catch (Exception e) {
            promise.reject(new Throwable(e.getMessage()));
        }
    }
}
