package com.medicalink.MedicaLink_backend.repositories;

import com.medicalink.MedicaLink_backend.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUserName(String userName);
}
