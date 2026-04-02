package com.springboot.relationship.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false , length = 50)
    private String name ;

    @Column(nullable = false , scale = 2 , precision = 19)
    private BigDecimal price ;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description ;

    @Column(name = "image_url", nullable = false)
    private String imageUrl ;
    @Column(nullable = false)
    private Integer stock ;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category ;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();










}
