package com.nawasenaproject.dawis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attendances")
public class Attendance {

    @Id
    private String id;

    private String date;

    private int specialWage;

    private int mandays;

    private int bonuses;

    private int advances;

    @Column(name = "paid_status")
    private String paidStatus;

    @ManyToOne
    @JoinColumn(name = "project_worker_id", referencedColumnName = "id")
    private ProjectWorker projectWorker;
}
