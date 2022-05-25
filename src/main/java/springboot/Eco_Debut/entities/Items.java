package springboot.Eco_Debut.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name",length = 200)
    private String name;

    @Column(name = "description",length = 200)
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "stars")
    private int stars;

    @Column(name = "smallPicURL",length = 200)
    private String smallPicURL;

    @Column(name = "largePicURL",length = 200)
    private String largePicURL;

    @Column(name = "addedDate",length = 200)
    private Date addedDate;

    @Column(name = "inTopPage",length = 200)
    private boolean inTopPage;

    @ManyToOne(fetch = FetchType.EAGER)
    private Types type;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Sizes> sizes;

    @ManyToOne(fetch = FetchType.EAGER)
    private Categories category;


}
