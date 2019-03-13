package com.cupster.netlistener_annotation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;

import com.cupster.netlistener_annotation.annotation.Network;
import com.cupster.netlistener_annotation.bean.MethodManager;

import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


class NetStateReceiver extends BroadcastReceiver {

   private static final String TAG = "NetStateReceiver";

    private NetType netType;
    private Map<String,INetChangeObserver> subscibers;
    private Map<Object , List<MethodManager>> networkList ;

    public NetStateReceiver() {
       netType = NetType.NONE;
       subscibers = new HashMap<>();
       networkList = new HashMap<>();
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
           Log.i(Constants.LOG_TAG, "network change to 11:"+ NetworkManager.getNetType());
           if (NetworkManager.isNetworkAvailable()){
               for ( INetChangeObserver listener : subscibers.values()){
                   listener.onConnect();
               }
           }else {
               for ( INetChangeObserver listener : subscibers.values()){
                   listener.onDisConnect();
               }
           }
           netType = NetworkManager.getNetType();
           Log.i(Constants.LOG_TAG, "onReceive: "+netType );
           postSubscribe(netType);
       }

   }


    public void registerObserver(Object o) {
        List<MethodManager>  methodList = networkList.get(o);
        if (methodList == null){//不为空，表示之前注册过
            methodList = findAnnotationMethod(o);
            Log.e(TAG, "registerObserver: "+methodList.size() );
            networkList.put(o ,methodList);
        }
    }

    public void unRegisterAllObserver() {
        if (networkList != null && !networkList.isEmpty() && networkList.size() != 0 ){
            networkList.clear();
        }
    }

    public void unRegisterObserver(Object o) {
        if (networkList != null && !networkList.isEmpty() && networkList.size() != 0 ){
            networkList.remove(o);
        }
    }

    /**
     * 找齐所有注解的方法
     * @param object
     * @return
     */
    private List<MethodManager> findAnnotationMethod(Object object) {
        List<MethodManager> methodList = new ArrayList<>();
        //1、反射获取类
        Class<?> clazz = object.getClass();
        //2、获取所有方法
        Method[] methods = clazz.getMethods();
        //3、循环获取指定方法
        for(Method method : methods){
            Network annotation = method.getAnnotation(Network.class);
            if (annotation == null){
                continue;
            }
            Type genericReturnType = method.getGenericReturnType();
            if (!"void".equals(genericReturnType.toString())){
                //不是指定 的 返回类型
                continue;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length !=1 ){
                //不是限定的 参数个数
                continue;
            }
            if (!parameterTypes[0].isAssignableFrom(NetType.class)){
                continue;
            }
            MethodManager manager = new MethodManager(parameterTypes[0] , annotation.netType() ,method);
            methodList.add(manager);

        }

        return methodList;
    }

    /**
     * 通知所有 注解方法
     * @param netType
     */
    private void postSubscribe(NetType netType) {
        Set<Object> objects = networkList.keySet();

        for (Object object: objects
             ) {
            List<MethodManager> managerList = networkList.get(object);
            if (managerList != null){

                for (MethodManager manager : managerList
                     ) {
                    switch (manager.getNetType()){
                        case AUTO:
                            invoke(manager ,object ,netType);
                            break;
                        case WIFI:
                            if (netType == NetType.NONE || netType == NetType.WIFI) {
                                invoke(manager ,object ,netType);
                            }
                            break;
                        case CMNET:
                            if (netType == NetType.NONE || netType == NetType.WIFI) {
                                invoke(manager ,object ,netType);
                            }
                            break;
                        case CMWAP:
                            if (netType == NetType.NONE || netType == NetType.WIFI) {
                                invoke(manager ,object ,netType);
                            }
                            break;
                        case NONE:
                            break;
                        default:
                            break;
                    }
                }
            }
        }


    }

    /**
     * 执行反射方法
     * @param manager
     * @param object
     * @param netType
     */
    private void invoke(MethodManager manager, Object object, NetType netType) {
        Method execute = manager.getMethod();
        try {
            execute.invoke(object ,netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
