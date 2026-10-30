package com.fashionshop.fashion_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "stores")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private String name;

    private String phone;

    private String address;

    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "store")
    private List<User> users;

    @OneToMany(mappedBy = "store")
    private List<StoreInventory> inventories;
}
