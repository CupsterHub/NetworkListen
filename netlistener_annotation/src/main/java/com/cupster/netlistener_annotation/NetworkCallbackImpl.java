package com.cupster.netlistener_annotation;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
 class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "NetworkCallbackImpl";

    private Callback4Lollipop callback;

//    public NetworkCallbackImpl() {
//    }

    public NetworkCallbackImpl(Callback4Lollipop callback) {
        this.callback = callback;
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.d(TAG, "onAvailable: 网络 连接");
        callback.available();
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        Log.d(TAG, "onCapabilitiesChanged: 网络 变化");
        //改方法可能一次变化 中  多次回调。
        callback.change();
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){//有效的网络
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                callback.available(NetType.WIFI);
                Log.d(TAG, "onCapabilitiesChanged: 网络 wifi");
            }else {
                Log.d(TAG, "onCapabilitiesChanged: 网络 其他");
                callback.available();
            }
            if (networkCapabilities.equals(NetworkCapabilities.NET_CAPABILITY_TRUSTED)){
                Log.d(TAG, "onCapabilitiesChanged: 可信任的网络");
            }
        }else {
            callback.unusable();
            Log.d(TAG, "onCapabilitiesChanged: 网络不可用");
        }
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Log.d(TAG, "onLost: 网络 断开");
        callback.lost();
    }

    //=----========    以上常用   =============

    @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
        Log.d(TAG, "onLosing: 网络 onLosing");
        callback.losing();
    }

    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network, linkProperties);
        Log.d(TAG, "onLinkPropertiesChanged: 网络 onLinkPropertiesChanged");
        callback.linkPropChange(linkProperties);
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
        callback.unusable();
        Log.d(TAG, "onUnavailable ");
    }


}
