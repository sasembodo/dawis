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
@Table(name = "project_worker") public class ProjectWorker {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @OneToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private Worker worker;

    @OneToMany(mappedBy = "projectWorker")
    private List<Attendance> attendances = new ArrayList<>();
}
