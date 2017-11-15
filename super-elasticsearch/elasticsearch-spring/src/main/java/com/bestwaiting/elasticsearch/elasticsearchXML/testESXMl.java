package com.bestwaiting.elasticsearch.elasticsearchXML;

import com.bestwaiting.elasticsearch.model.Book;
import com.bestwaiting.elasticsearch.repository.BookRepository;
import com.sun.tools.corba.se.idl.constExpr.BooleanOr;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by bestwaiting on 17/11/4.
 */
public class testESXMl {

    static void createBook(BookRepository bookRepository) {
        for (int i = 6; i < 10; i++) {
            Book book = new Book();
            book.setId(String.valueOf(i));
            book.setName("book" + i);
            book.setPrice(i + 10L);
            book.setVersion((long) i);
            bookRepository.save(book);
        }
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"spring-elasticsearch.xml"});
        context.start();
        BookRepository bookRepository = context.getBean("bookRepository", BookRepository.class);
        createBook(bookRepository);
        Iterable<Book> books = bookRepository.findAll();
        for (Book book : books) {
            System.out.println(book.getName());
        }
    }
}
