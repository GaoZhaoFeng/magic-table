package com.huawei.magictable.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Entity
@Table(name = "book_info")
@ToString
public class BookInfo {
  @Id
  @GeneratedValue(generator  = "myIdStrategy")
  @GenericGenerator(name = "myIdStrategy", strategy = "uuid")
  private String  id;
  private String bookName;
  private String author;
  private double price;
  private String publisher;
  private  String book_time;
  private String isbn;
  private String type;
  private String url;
}
