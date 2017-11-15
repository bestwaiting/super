package com.bestwaiting.dao;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * Created by bestwaiting on 17/6/3.
 */
@ContextConfiguration(classes = DaoContext.class)
public class Main {
    private static AnnotationConfigApplicationContext CONTEXT;


    public static void main(String[] args) {
        CONTEXT = new AnnotationConfigApplicationContext(DaoContext.class);
//        PersonService personService = (PersonService) CONTEXT.getBean("personService");
//        Logger logger = LoggerFactory.getLogger(Main.class);
//        logger.warn("dsdsdsads");
//        PageHelper.startPage(1, 10);
//        PageHelper.orderBy("id desc");
//        List<Person> list = personService.select();
//        //用PageInfo对结果进行包装
//        PageInfo page = new PageInfo(list);
//        //测试PageInfo全部属性
//        //PageInfo包含了非常全面的分页属性
//        assertEquals(1, page.getPageNum());
//        assertEquals(10, page.getPageSize());
//        assertEquals(1, page.getStartRow());
//        assertEquals(10, page.getEndRow());
//        assertEquals(183, page.getTotal());
//        assertEquals(19, page.getPages());
//        assertEquals(false, page.isHasPreviousPage());
//        assertEquals(true, page.isHasNextPage());
    }

}

