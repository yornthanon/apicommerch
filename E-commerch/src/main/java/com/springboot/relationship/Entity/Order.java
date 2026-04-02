package com.springboot.relationship.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_time" )
    @CreationTimestamp
    private LocalDateTime date;

    @Column(nullable = false , name = "total_amount" , scale = 2 , precision = 19)

    private BigDecimal totalAmount;

    @Column(nullable = false , columnDefinition = "TEXT")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order" , cascade = CascadeType.ALL)
    private Payment payment;









}
