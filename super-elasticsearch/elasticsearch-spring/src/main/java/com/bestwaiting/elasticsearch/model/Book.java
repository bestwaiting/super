package com.bestwaiting.elasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by bestwaiting on 17/11/4.
 */
@Document(indexName = "book", type = "book")
public class Book {
    @Id
    private String id;
    @Field(type = FieldType.text)
    private String name;
    @Field(type = FieldType.Long)
    private Long price;
    @Version
    private Long version;

    public Book() {
    }

    public Book(String id, String name, Long version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}

