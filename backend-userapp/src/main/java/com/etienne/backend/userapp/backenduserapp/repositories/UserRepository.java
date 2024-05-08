package com.etienne.backend.userapp.backenduserapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.etienne.backend.userapp.backenduserapp.models.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
