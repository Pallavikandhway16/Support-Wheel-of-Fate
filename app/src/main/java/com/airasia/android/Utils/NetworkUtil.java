package com.airasia.android.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.airasia.android.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NetworkUtil {
    public static boolean checkNetworkConnection(Context context) {
        boolean mobileDataEnabled = false;
        boolean networkEnable = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoWiif = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        try {
            Class cmClass = Class.forName(connectivityManager.getClass().getName());
            Method method = cmClass
                    .getDeclaredMethod(
                            context.getResources().getString(R.string.is_mobile_data_enable));
            method.setAccessible(true);
            mobileDataEnabled = (Boolean) method.invoke(connectivityManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        networkEnable = infoWiif.isConnected() || mobileDataEnabled;
        return networkEnable;
    }
}
