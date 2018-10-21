package com.design.extendByCondition.service;

import com.design.extendByCondition.bean.SupportBean;

public interface Service {

    void execute();

    int getPriority();

    boolean isSupport(SupportBean supportBean);
}
