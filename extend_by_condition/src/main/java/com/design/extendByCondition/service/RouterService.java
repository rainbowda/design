package com.design.extendByCondition.service;

import com.design.extendByCondition.bean.SupportBean;

public interface RouterService {

    void execute();


    boolean isSupport(SupportBean supportBean);
}
