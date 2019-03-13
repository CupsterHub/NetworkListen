package com.cupster.networklisten;

import android.app.Application;
import android.content.Context;
import android.net.LinkProperties;
import android.os.Build;

import com.cupster.netlistener_annotation.Callback4Lollipop;
import com.cupster.netlistener_annotation.NetType;
import com.cupster.netlistener_annotation.NetworkManager;

import java.util.ArrayList;
import java.util.List;

//import com.cupster.netlistener.NetworkManager;

public class MyApplication extends Application {

    public interface onNetListener{
        void available();
        void unusable();
        void lost();
        void change();
    }

    private static Context context;

    private static List<onNetListener> subs = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //普通代码方式 观察者模式
//        NetworkManager.getInstance().init(context);

        //注解方式1 广播
        NetworkManager.getInstance().init(context);
//        注解方式2 接口回调
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            NetworkManager.getInstance().init(context, callback);
//        }
    }

    public static Context getMyAppContext(){
        return context;
    }

    Callback4Lollipop callback =  new Callback4Lollipop() {
        @Override
        public void available(NetType type) {
            for (onNetListener listener :subs){
                listener.available();
            }
        }

        @Override
        public void available() {
            for (onNetListener listener :subs){
                listener.available();
            }
        }

        @Override
        public void unusable() {
            for (onNetListener listener :subs){
                listener.unusable();
            }
        }

        @Override
        public void lost() {
            for (onNetListener listener :subs){
                listener.lost();
            }
        }

        @Override
        public void losing() {

        }

        @Override
        public void linkPropChange(LinkProperties linkProperties) {

        }

        @Override
        public void change() {
            for (onNetListener listener :subs){
                listener.change();
            }
        }
    };

    public static void addNetlistener(onNetListener listener){
        subs.add(listener);
    }

    public static void removeNetlistener(onNetListener listener){
        subs.remove(listener);
    }

    public static void removeAll(){
        subs.clear();
    }

}
