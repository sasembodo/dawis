package com.nawasenaproject.dawis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(unique = true)
    private String username;

    private String password;

    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_at")
    private String modifiedAt;

    @OneToMany(mappedBy = "user")
    private List<Worker> workers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Project> projects = new ArrayList<>();
}
