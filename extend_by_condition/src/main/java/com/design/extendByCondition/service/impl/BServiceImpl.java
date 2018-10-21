package com.design.extendByCondition.service.impl;

import com.design.extendByCondition.bean.SupportBean;
import com.design.extendByCondition.service.Service;
import org.springframework.stereotype.Component;

@Component
public class BServiceImpl implements Service {
    @Override
    public void execute() {
        System.out.println("B execute");
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isSupport(SupportBean supportBean) {

        return supportBean.getSupportNum() % 3 == 1;
    }
}
