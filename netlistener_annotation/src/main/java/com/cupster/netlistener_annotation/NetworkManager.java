package com.cupster.netlistener_annotation;


import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class NetworkManager {

    private static Context mContext;
    private NetStateReceiver receiver;

    private NetworkManager(){
        receiver = new NetStateReceiver();
    }

    /**
     * 反注册
     * @param o activity or fragment
     */
    public void unRegisterObserver(Object o) {
        receiver.unRegisterObserver(o);
    }

    /**
     * 反注解all
     */
    public void unRegisterAllObserver() {
        receiver.unRegisterAllObserver();
    }

    /**
     * 注册
     * @param o activity or fragment
     */
    public void registerObserver(Object o) {
        receiver.registerObserver(o);
    }

    private static class holder {
        public static NetworkManager instance = new NetworkManager();
    }

    public static NetworkManager getInstance(){
        return holder.instance;
    }

    /**
     * 接口回调方式
     *
     * @param context
     * @param callback4Lollipop
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void init(Context context ,Callback4Lollipop callback4Lollipop){
        ConnectivityManager.NetworkCallback networkCallback = new NetworkCallbackImpl(callback4Lollipop);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        NetworkRequest request = builder.build();
        ConnectivityManager conn = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null){
            conn.registerNetworkCallback(request,networkCallback);
        }
    }


    /**
     * 广播方式
     * @param context
     */
    public void init(Context context){
        mContext = context;
        IntentFilter filter = new IntentFilter(Constants.ANDROID_NET_CHANGE_ACTION);
        mContext.registerReceiver(receiver,filter);
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
