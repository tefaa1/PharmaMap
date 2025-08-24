package com.pharmamap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pharmacy")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "categorySet")
@ToString(exclude = "categorySet")
public class Pharmacy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false,length = 20)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @OneToMany(mappedBy = "pharmacy",cascade = CascadeType.ALL,orphanRemoval = true)
    Set<Category> categorySet =new HashSet<>();

    public void addCategory(Category category){
        this.categorySet.add(category);
        category.setPharmacy(this);
    }

    public void removeCategory(Category category){
        this.categorySet.remove(category);
        category.setPharmacy(null);
    }
}
