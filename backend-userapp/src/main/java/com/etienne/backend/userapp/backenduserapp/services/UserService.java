package com.etienne.backend.userapp.backenduserapp.services;

import java.util.List;
import java.util.Optional;

import com.etienne.backend.userapp.backenduserapp.models.dto.UserDto;
import com.etienne.backend.userapp.backenduserapp.models.entities.User;

public interface UserService  {
    List<UserDto> findAll();
    Optional<UserDto> findById(Long id);
    UserDto save(User user);
    Optional<UserDto> update(User user, Long id);
    void remove(Long id);
}
