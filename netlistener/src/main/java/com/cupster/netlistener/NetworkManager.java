package com.cupster.netlistener;


import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络监听
 */
public class NetworkManager {

    private static Context mContext;
    private NetStateReceiver receiver;

    private NetworkManager(){
        receiver = new NetStateReceiver();
    }

    private static class holder {
        public static NetworkManager instance = new NetworkManager();
    }

    public static NetworkManager getInstance(){
        return holder.instance;
    }

    public void init(Context context){
        mContext = context;
        IntentFilter filter = new IntentFilter(Constants.ANDROID_NET_CHANGE_ACTION);
        mContext.registerReceiver(receiver,filter);
    }

    public void addSubscriber(String tag ,INetChangeObserver subscriber){
        receiver.addListener(tag ,subscriber);
    }

    public void removeSubscriber(String tag){
        receiver.removeListener(tag);
    }


    public static boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null){
            return false;
        }
        NetworkInfo[] infos = manager.getAllNetworkInfo();
        if (infos != null){
            for ( NetworkInfo info :infos){
                if (info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

    public static NetType getNetType(){
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager ==null){
            return NetType.NONE;
        }
        NetworkInfo info  = manager.getActiveNetworkInfo();
        if (info ==null){
            return NetType.NONE;
        }
        int nType = info.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE){
            if (info.getExtraInfo().toLowerCase().equals("cmnet")){
                return NetType.CMNET;
            }else {
                return NetType.CMWAP;
            }
        }else if (nType == ConnectivityManager.TYPE_WIFI){
            return NetType.WIFI;
        }
        return NetType.NONE;
    }

}
