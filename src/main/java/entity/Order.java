package entity;

import entity.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private Double offerPrice;
    @Column(unique = true)
    private String description;
    private LocalDateTime orderRegisterTime;
    @Column(unique = true)
    private String address;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    @OneToMany
    private Set<Offer> offers;
}
