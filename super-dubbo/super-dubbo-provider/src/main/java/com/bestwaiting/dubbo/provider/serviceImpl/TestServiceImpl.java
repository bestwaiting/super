package com.bestwaiting.dubbo.provider.serviceImpl;

import com.bestwaiting.dubbo.api.service.TestService;
import org.springframework.stereotype.Service;

/**
 * Created by bestwaiting on 17/7/3.
 */
@Service("testService")
public class TestServiceImpl implements TestService {
    public String dubboTest(String name) {
        return "hello " + name;
    }
}
