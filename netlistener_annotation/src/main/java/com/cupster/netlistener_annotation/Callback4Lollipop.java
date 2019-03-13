package com.cupster.netlistener_annotation;

import android.net.LinkProperties;

public interface Callback4Lollipop {

    void available(NetType type);

    void available();

    void unusable();

    void lost();

    void losing();

    void linkPropChange(LinkProperties linkProperties);

    void change();
}
