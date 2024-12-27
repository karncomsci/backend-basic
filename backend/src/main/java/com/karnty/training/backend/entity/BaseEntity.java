package com.karnty.training.backend.entity;



import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid") // Specify custom UUID generator
    @GenericGenerator(name = "uuid", strategy = "uuid2") // Hibernate-specific generator for UUID
    @Column(name = "id",length = 36, updatable = false, nullable = false)
    private String id;
}
