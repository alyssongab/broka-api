package br.com.apibroka.broka.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name="restaurants")
@Entity
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String address;

    @Column(name = "cuisine_type")
    private String cuisineType;

    // muitos restaurantes para um dono
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // restaurante tem muitos produtos
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();
}
