package com.karnty.training.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "m_user")
public class User extends BaseEntity{

    @Column(name = "email", nullable = false, unique = true,length = 60)
    private String email;
    @Column(name = "password", nullable = false, length = 120)
    private String password;
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @OneToOne(mappedBy = "user",orphanRemoval = true)
    private Social social;

    @OneToMany(mappedBy = "user",orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Address> addresses;
}
