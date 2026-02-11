package br.com.projedata.models;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class RawMaterial extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String code;

    public String name;

    public Integer stockQuantity;

    @OneToMany(mappedBy = "rawMaterial")
    public List<ProductMaterial> products = new ArrayList<>();
}
