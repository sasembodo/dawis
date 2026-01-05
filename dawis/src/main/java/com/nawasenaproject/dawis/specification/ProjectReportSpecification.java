package com.nawasenaproject.dawis.specification;

import com.nawasenaproject.dawis.entity.Attendance;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ProjectReportSpecification {

    public static Specification<Attendance> belongsToProject(String projectId) {
        return (root, query, builder) ->
                builder.equal(root.get("projectWorker").get("project").get("id"), projectId);
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

}
