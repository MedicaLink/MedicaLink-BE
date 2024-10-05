package com.medicalink.MedicaLink_backend.repositories;

import com.medicalink.MedicaLink_backend.models.UserSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
    @Query(value = "nextval(pg_get_serial_sequence('your_table_name', 'id'))", nativeQuery = true)
    Long findNextGeneratedId();
}
