package com.design.extendByCondition.service.impl;

import com.design.extendByCondition.bean.SupportBean;
import com.design.extendByCondition.service.Service;
import org.springframework.stereotype.Component;

@Component
public class DServiceImpl implements Service {
    @Override
    public void execute() {
        System.out.println("D execute");
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public boolean isSupport(SupportBean supportBean) {

        return supportBean.getSupportNum() % 3 == 0;
    }
}
