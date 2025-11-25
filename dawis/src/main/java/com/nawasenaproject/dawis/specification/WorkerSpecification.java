package com.nawasenaproject.dawis.specification;

import com.nawasenaproject.dawis.entity.Worker;
import com.nawasenaproject.dawis.util.DateUtil;
import com.nawasenaproject.dawis.util.SafeConverterUtil;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Objects;

public class WorkerSpecification {

    public static Specification<Worker> recruitDateBetween(String start, String end) {
        return (root, query, builder) -> {
            if (start == null || start.isEmpty() || end == null || end.isEmpty()) {
                return null;
            }
            LocalDate startDate = DateUtil.stringToLocalDate(start);
            LocalDate endDate = DateUtil.stringToLocalDate(end);
            return builder.between(root.get("recruitDate"), startDate, endDate);
        };
    }

    public static Specification<Worker> positionEquals(String position) {
        return (root, query, builder) -> {
            if (position == null || position.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("position")), "%" + position + "%" );
        };
    }

    public static Specification<Worker> wageBetween(String min, String max) {
        return (root, query, builder) -> {
            if (min == null || min.isEmpty() || max == null || max.isEmpty()) {
                return null;
            }

            Integer minWage = SafeConverterUtil.toIntegerSafe(min);
            Integer maxWage = SafeConverterUtil.toIntegerSafe(max);

            if (minWage == null || maxWage == null) {
                return null;
            }

            return builder.between(root.get("wage"), minWage, maxWage);
        };
    }

    public static Specification<Worker> belongsToUser(String username) {
        return (root, query, builder) ->
                builder.equal(root.get("user").get("username"), username);
    }
}
