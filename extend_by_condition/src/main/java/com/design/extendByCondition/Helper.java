package com.design.extendByCondition;

import com.design.extendByCondition.bean.SupportBean;
import com.design.extendByCondition.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class Helper {

    @Autowired
    private List<Service> services;

    public void execute(SupportBean supportBean){

        Service s = services.stream()
                .filter((service) -> service.isSupport(supportBean))
                .sorted(Comparator.comparing(Service::getPriority))
                .map(Optional::ofNullable)
                .findFirst()
                .flatMap(Function.identity())
                .orElse(null);


        if (s != null){
            s.execute();
        }

        System.out.println(s);

    }
}
