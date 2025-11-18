package com.nawasenaproject.dawis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    private String id;

    private String code;

    private String name;

    private String type;

    private String location;

    private String coordinates;

    private int status;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "finish_date")
    private String finishDate;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @OneToOne(mappedBy = "project")
    private ProjectWorker projectWorker;
}
