package com.nawasenaproject.dawis.specification;

import com.nawasenaproject.dawis.entity.Project;
import com.nawasenaproject.dawis.util.DateUtil;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ProjectSpecification {

    public static Specification<Project> nameEquals(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("name")), "%" + name + "%" );
        };
    }

    public static Specification<Project> codeEquals(String code) {
        return (root, query, builder) -> {
            if (code == null || code.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.upper(root.get("code")), "%" + code + "%" );
        };
    }

    public static Specification<Project> typeEquals(String type) {
        return (root, query, builder) -> {
            if (type == null || type.isEmpty()) {
                return null;
            }
            return builder.equal(root.get("type"), type.toUpperCase());
        };
    }

    public static Specification<Project> locationEquals(String location) {
        return (root, query, builder) -> {
            if (location == null || location.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("location")), "%" + location + "%" );
        };
    }

    public static Specification<Project> statusEquals(String status) {
        return (root, query, builder) -> {
            if (status == null || status.isEmpty()) {
                return null;
            }
            return builder.equal(root.get("status"), Integer.valueOf(status));
        };
    }

    public static Specification<Project> startDateBetween(String begin, String end) {
        return (root, query, builder) -> {
            if (begin == null || begin.isEmpty() || end == null || end.isEmpty()) {
                return null;
            }
            LocalDate beginDate = DateUtil.stringToLocalDate(begin);
            LocalDate endDate = DateUtil.stringToLocalDate(end);
            return builder.between(root.get("startDate"), beginDate, endDate);
        };
    }

    public static Specification<Project> finishDateBetween(String begin, String end) {
        return (root, query, builder) -> {
            if (begin == null || begin.isEmpty() || end == null || end.isEmpty()) {
                return null;
            }
            LocalDate beginDate = DateUtil.stringToLocalDate(begin);
            LocalDate endDate = DateUtil.stringToLocalDate(end);
            return builder.between(root.get("finishDate"), beginDate, endDate);
        };
    }

    public static Specification<Project> belongsToUser(String username) {
        return (root, query, builder) ->
                builder.equal(root.get("user").get("username"), username);
    }
}
