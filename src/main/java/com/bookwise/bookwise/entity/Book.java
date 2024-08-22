package com.bookwise.bookwise.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Column(name = "avl_qty")
    private Long avlQty = 0L;

    private String image;


//    @JoinColumn(name = "category", referencedColumnName = "id")
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
//    private Long category;

}
