package com.bestwaiting.multidao;

import com.bestwaiting.multidao.datasource.DataSourceContext;
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
        DataSourceContext.setDataSourceType("test");
//        PersonService personService=(PersonService)CONTEXT.getBean("personService");
//
//        Logger logger = LoggerFactory.getLogger(Main.class);
//        logger.warn("dsdsdsads");
//        PageHelper.startPage(1, 10);
//        PageHelper.orderBy("id desc");
//        List<Person> list = personService.select();
//        //用PageInfo对结果进行包装
//        PageInfo page = new PageInfo(list);
//        System.out.println(page.getList().size());
    }

}
