package com.design.extendByCondition.service.impl;

import com.design.extendByCondition.bean.SupportBean;
import com.design.extendByCondition.service.RouterService;
import org.springframework.stereotype.Component;

@Component
public class CRouterServiceImpl implements RouterService {
    @Override
    public void execute() {
        System.out.println("C router execute");
    }

    @Override
    public boolean isSupport(SupportBean supportBean) {

        return supportBean.getSupportNum() % 3 == 2;
    }
}
