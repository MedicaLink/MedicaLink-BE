package com.medicalink.MedicaLink_backend.fhir;

import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class PatientSearchSpecification {
    public static Specification<PatientSearchParams> withNic(String nic) {
        return (root, _, builder) ->
                builder.like(builder.lower(root.get("nic")), "%" + nic + "%");
    }

    public static Specification<PatientSearchParams> withName(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%"+ name + "%");
    }

    public static Specification<PatientSearchParams> withAddress(String address) {
        return (root, _, builder) ->
                builder.like(builder.lower(root.get("address")), "%" + address + "%");
    }

    public static Specification<PatientSearchParams> withBirthday(Date birthday, Operation operation) {
        return (root, _, builder) -> (
                switch (operation) {
                    case Operation.MORE_THAN -> builder.greaterThan(root.get("birthdate"), birthday);
                    case Operation.LESS_THAN -> builder.lessThan(root.get("birthdate"), birthday);
                    default -> builder.equal(root.get("birthdate"), birthday);
                }
        );
    }

    public enum Operation {
        MORE_THAN,
        LESS_THAN,
        EQUALS,
        IN_BETWEEN
    }
}
