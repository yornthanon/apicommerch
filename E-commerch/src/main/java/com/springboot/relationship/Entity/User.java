package com.springboot.relationship.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , nullable = false ,length = 50)
    private String username;
    @Column(unique = true ,nullable = false ,length = 100)
    private String email;
    @Column(nullable = false ,length = 30)
    private String password;


    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")

    )
    private List<Role>  roles = new ArrayList<>();

    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL)

    private List<Order> orders = new ArrayList<>();







}
