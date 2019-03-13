package com.cupster.networklisten;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

//import com.cupster.netlistener.INetChangeObserver;
//import com.cupster.netlistener.NetworkManager;
import com.cupster.netlistener_annotation.NetType;
import com.cupster.netlistener_annotation.annotation.Network;

/**
 * 订阅者
 */
//public class MainActivity extends AppCompatActivity  implements INetChangeObserver {
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        NetworkManager.getInstance().addSubscriber(TAG,this);

        //方式二：注解
        com.cupster.netlistener_annotation.NetworkManager.getInstance().registerObserver(this);

//        //方式3：接口监听 api 21+
//        MyApplication.addNetlistener(listener);
    }

//    @Override
//    public void onConnect() {
//        Log.i(TAG, "onConnect: =========");
//    }
//
//    @Override
//    public void onDisConnect() {
//        Log.i(TAG, "onDisConnect: ----------------");
//    }

    /**
     *
     * 方式二  注解
     *
     */
    @Network(netType = NetType.AUTO)
    public void network(NetType netType){
        switch (netType){
            case WIFI:
                Log.e(TAG, "network: wifi" );
                break;
            case CMNET:
                Log.e(TAG, "network: cm net" );
                break;
            case CMWAP:
                Log.e(TAG, "network: wap" );
                break;
            case NONE:
                Log.e(TAG, "network: none" );
                break;
            default:
                break;
        }
    }

    /**
     * 方式3  注解+接口方式
     */
    MyApplication.onNetListener listener = new MyApplication.onNetListener() {
        @Override
        public void available() {
            Log.e(TAG, "available: " );
        }

        @Override
        public void unusable() {
            Log.e(TAG, "unusable: " );
        }

        @Override
        public void lost() {
            Log.e(TAG, "lost: ");
        }

        @Override
        public void change() {
            Log.e(TAG, "change: " );
        }
    };

    @Override
    protected void onDestroy() {
        com.cupster.netlistener_annotation.NetworkManager.getInstance().unRegisterObserver(this);
        //方式3
//        MyApplication.removeNetlistener(listener);
        super.onDestroy();
    }
}
