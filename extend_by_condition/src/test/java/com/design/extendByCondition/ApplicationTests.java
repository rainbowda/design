package com.design.extendByCondition;

import com.design.extendByCondition.bean.SupportBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private Helper Helper;

    @Test
    public void contextLoads() {
        Helper.execute(new SupportBean(3));
    }

}
