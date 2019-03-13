package com.cupster.netlistener;

public enum NetType {
    /**
     * 有网络，包括WiFi / GPRS
     */
    AUTO,

    /**
     * WIFI网络
     */
    WIFI,

    /**
     * 主要是PC 、笔记本电脑 、pad
     */
    CMNET,

    /**
     * 手机
     */
    CMWAP,

    /**
     * 没有任何网络
     */
    NONE
}
