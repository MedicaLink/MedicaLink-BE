package com.medicalink.MedicaLink_backend.services;

import com.medicalink.MedicaLink_backend.models.User;
import com.medicalink.MedicaLink_backend.repositories.UserRepository;
import com.medicalink.MedicaLink_backend.utils.HttpExceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a single user from the database
     * @param userName the username of the user
     */
    public User getUser(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("User with user name '%s' was not found", userName
                                )
                        ));
    }

    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                            String.format("User with user name '%s' was not found", id
                            )
                ));
    }
}
