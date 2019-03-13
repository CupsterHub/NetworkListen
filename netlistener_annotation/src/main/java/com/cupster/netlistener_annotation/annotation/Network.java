package com.cupster.netlistener_annotation.annotation;


import com.cupster.netlistener_annotation.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//作用在方法之上
@Retention(RetentionPolicy.RUNTIME)//运行时通过反射获得注解的方法
public @interface Network {

    NetType netType() default NetType.AUTO;

}
