package com.cnpm.entity;

import com.cnpm.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonManagedReference;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "[User]")
public abstract class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long userId;
    @Column(length = 150, columnDefinition = "nvarchar(150)")
    protected String fullName;
    protected String phone;
    @Enumerated(EnumType.STRING)
    protected Gender gender;
    @Column(columnDefinition = "nvarchar(max)")
    protected String address;
    @Column(columnDefinition = "nvarchar(max)")
    protected String email;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "accountId")
    @JsonManagedReference
    protected Account account;

}
