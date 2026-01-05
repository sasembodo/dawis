package com.nawasenaproject.dawis.specification;

import com.nawasenaproject.dawis.entity.Attendance;
import com.nawasenaproject.dawis.enums.PaidStatus;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ReportSpecification {

    public static Specification<Attendance> belongsToWorker(String workerId) {
        return (root, query, builder) ->
                builder.equal(root.get("projectWorker").get("worker").get("id"), workerId);
    }

    public static Specification<Attendance> belongsToProject(String projectId) {
        return (root, query, builder) ->
                builder.equal(root.get("projectWorker").get("project").get("id"), projectId);
    }

    public static Specification<Attendance> dateBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return null;

        return (root, query, builder) ->
                builder.between(root.get("date"), start, end);
    }

    public static Specification<Attendance> projectCodeEquals(String code) {
        if (code == null || code.isBlank()) return null;

        return (root, query, builder) ->
                builder.equal(root.get("projectWorker").get("project").get("code"), code);
    }

    public static Specification<Attendance> workerNipEquals(String workerNip) {
        if (workerNip == null || workerNip.isBlank()) return null;

        return (root, query, builder) ->
                builder.equal(root.get("projectWorker").get("worker").get("nip"), workerNip);
    }

    public static Specification<Attendance> belongsToUser(String username) {
        return (root, query, builder) -> {

            if (query != null) {
                query.distinct(true);
            }

            Join<?, ?> pwJoin = root.join("projectWorker", JoinType.INNER);
            Join<?, ?> projectJoin = pwJoin.join("project", JoinType.INNER);
            Join<?, ?> userJoin = projectJoin.join("user", JoinType.INNER);

            return builder.equal(userJoin.get("username"), username);
        };
    }

    public static Specification<Attendance> paidStatusEqualsUnpaid() {

        return (root, query, builder) ->
                builder.equal(root.get("paidStatus"), PaidStatus.UNPAID.getCode());
    }
}
