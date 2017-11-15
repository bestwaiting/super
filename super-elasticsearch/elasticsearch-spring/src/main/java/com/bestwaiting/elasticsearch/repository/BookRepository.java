package com.bestwaiting.elasticsearch.repository;

import com.bestwaiting.elasticsearch.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by bestwaiting on 17/11/4.
 */
@Repository("bookRepository")
public interface BookRepository extends CrudRepository<Book, String> {

    List<Book> findByName(String name);

    List<Book> findByPrice(double price);
}
