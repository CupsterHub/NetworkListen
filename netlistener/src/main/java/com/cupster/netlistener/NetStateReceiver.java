package com.cupster.netlistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


 class NetStateReceiver extends BroadcastReceiver {

    private static final String TAG = "NetStateReceiver";

    private NetType netType;
    private Map<String,INetChangeObserver> subscibers;

    public NetStateReceiver() {
        netType = NetType.NONE;
        subscibers = new HashMap<>();
    }

    public void addListener(String tag ,INetChangeObserver listener){
        if (TextUtils.isEmpty(tag) || listener == null){
            return;
        }
        subscibers.put(tag ,listener);
    }

    public void removeListener(String tag){
        if (TextUtils.isEmpty(tag) || !subscibers.containsKey(tag)){
            return;
        }
        subscibers.remove(tag);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())){
            Log.e(Constants.LOG_TAG, "err happen" );
            return;
        }
        if (intent.getAction().equals(Constants.ANDROID_NET_CHANGE_ACTION)){
            Log.i(Constants.LOG_TAG, "network change to :"+ NetworkManager.getNetType());
            if (NetworkManager.isNetworkAvailable()){
                for ( INetChangeObserver listener : subscibers.values()){
                    listener.onConnect();
                }
            }else {
                for ( INetChangeObserver listener : subscibers.values()){
                    listener.onDisConnect();
                }
            }
        }

    }



}
