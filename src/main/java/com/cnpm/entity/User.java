package com.cnpm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
    protected String gender;
    @Column(columnDefinition = "nvarchar(max)")
    protected String address;
    protected String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    protected Account account;

}
