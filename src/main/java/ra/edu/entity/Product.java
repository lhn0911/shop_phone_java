package ra.edu.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, precision = 12, scale = 2)
    private double price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String image;

    private boolean status;

}
