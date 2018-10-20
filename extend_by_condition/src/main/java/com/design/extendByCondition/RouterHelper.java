package com.design.extendByCondition;

import com.design.extendByCondition.bean.SupportBean;
import com.design.extendByCondition.service.RouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterHelper {

    @Autowired
    private List<RouterService> routers;

    public void execute(SupportBean supportBean){

        RouterService routerService = routers.stream()
                .filter((router) -> router.isSupport(supportBean))
                .findFirst()
                .orElseGet(null);


        if (routerService != null){
            routerService.execute();
        }

    }
}
