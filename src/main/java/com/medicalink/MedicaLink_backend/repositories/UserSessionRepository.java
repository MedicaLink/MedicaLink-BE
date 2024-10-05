package com.medicalink.MedicaLink_backend.repositories;

import com.medicalink.MedicaLink_backend.models.UserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, UUID> {
}
