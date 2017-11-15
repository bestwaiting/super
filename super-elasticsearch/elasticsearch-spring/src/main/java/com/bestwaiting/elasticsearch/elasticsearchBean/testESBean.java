package com.bestwaiting.elasticsearch.elasticsearchBean;

import com.bestwaiting.elasticsearch.model.Book;
import com.bestwaiting.elasticsearch.repository.BookRepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * Created by bestwaiting on 17/11/4.
 */
public class testESBean {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                ElasticsearchBean.class);
        context.scan("com.bestwaiting.elasticsearch");
        TestL testL = context.getBean(TestL.class);
        testL.ll();
        BookRepository bookRepository = context.getBean(BookRepository.class);
        Iterable<Book> books = bookRepository.findAll();
        for (Book book : books) {
            System.out.println(book.getName());
        }
    }
}
