package com.cnpm.entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long accountId;
    @Column(unique = true)
    protected String username;
    protected String password;


    @OneToOne(mappedBy = "account")
    @JsonBackReference
    protected User user;

    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "roleId")
    protected Role role;
}
