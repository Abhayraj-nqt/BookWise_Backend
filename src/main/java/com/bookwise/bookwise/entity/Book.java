package com.bookwise.bookwise.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString
@RequiredArgsConstructor
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    @Column(name = "total_qty")
    private Long totalQty = 1L;

    @Column(name = "avl_qty")
    private Long avlQty = 0L;

    private String image;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

}
