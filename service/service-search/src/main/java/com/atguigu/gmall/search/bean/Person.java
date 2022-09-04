package com.atguigu.gmall.search.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author: cxz
 * @create； 2022-09-03 22:38
 **/
@Document(indexName = "person")
@Data
public class Person {
    @Id
    private Long id;
    @Field(type = FieldType.Keyword)
    private String name;

    private Long age;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String address;
}
