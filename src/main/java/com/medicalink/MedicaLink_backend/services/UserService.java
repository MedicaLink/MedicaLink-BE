package com.medicalink.MedicaLink_backend.services;

import com.medicalink.MedicaLink_backend.models.User;
import com.medicalink.MedicaLink_backend.repositories.UserRepository;
import com.medicalink.MedicaLink_backend.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser() {
        //TODO: Implement
    }

    public User getUser(String userName) {
        var user = userRepository.findByUserName(userName)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("User with user name '%s' was not found", userName
                                )
                        ));

        return user;
    }
}
