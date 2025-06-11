package ra.edu.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String phone;

    @Column(length = 100, unique = true)
    private String email;
    private String address;
}
