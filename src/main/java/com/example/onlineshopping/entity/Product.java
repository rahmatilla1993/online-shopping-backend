package com.example.onlineshopping.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "product")
@EntityListeners(value = AuditingEntityListener.class)
public class Product extends BaseEntity {

    private float price;

    private String description;

    @ElementCollection(targetClass = String.class)
    @CollectionTable(
            name = "product_images",
            joinColumns = {@JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id"
            )}
    )
    private List<String> imageUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate validateDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @ManyToOne(optional = false)
    private Category category;

    @ManyToOne(optional = false)
    private Brand brand;
}
