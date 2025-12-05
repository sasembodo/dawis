package com.nawasenaproject.dawis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attendances")
public class Attendance {

    @Id
    private String id;

    private LocalDate date;

    @Column(name = "special_wage", nullable = true)
    private Integer specialWage;

    private Double mandays;

    @Column(nullable = true)
    private Integer bonuses;

    @Column(nullable = true)
    private Integer advances;

    @Column(name = "paid_status")
    private Integer paidStatus;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "project_worker_id", referencedColumnName = "id")
    private ProjectWorker projectWorker;
}
