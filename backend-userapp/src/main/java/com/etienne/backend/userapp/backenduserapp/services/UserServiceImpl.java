package com.etienne.backend.userapp.backenduserapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etienne.backend.userapp.backenduserapp.models.dto.UserDto;
import com.etienne.backend.userapp.backenduserapp.models.dto.MapperDto.DtoMapperUser;
import com.etienne.backend.userapp.backenduserapp.models.entities.Role;
import com.etienne.backend.userapp.backenduserapp.models.entities.User;
import com.etienne.backend.userapp.backenduserapp.repositories.RoleRepository;
import com.etienne.backend.userapp.backenduserapp.repositories.UserRepository;
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
       List<User> users = (List<User>) repository.findAll();
       return users
       .stream()
       .map(u -> DtoMapperUser.builder().setUser(u).build())
       .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        return repository.findById(id).map(u->DtoMapperUser.builder().setUser(u).build());
        // if(o.isPresent()){
        //     return Optional.of(DtoMapperUser.builder().setUser(o.orElseThrow()).build());
        // }
        // return Optional.empty();
    }

    @Override
    @Transactional
    public UserDto save(User user) {
        String passwordBc = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordBc);
        Optional<Role> o = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        if(o.isPresent()){
            roles.add(o.orElseThrow());
        }
        user.setRoles(roles);
        return DtoMapperUser.builder().setUser(repository.save(user)).build() ;
    }
    @Override
    @Transactional
    public Optional<UserDto> update(User user, Long id) {
        Optional<User> exist = repository.findById(id);
        User userOptional = null;
        if(exist.isPresent()){
            User userDb = exist.orElseThrow();
            userDb.setUsername(user.getUsername());
            userDb.setEmail(user.getEmail());
            userOptional = repository.save(userDb);

        }
        
        return Optional.ofNullable(DtoMapperUser.builder().setUser(userOptional).build());
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }
}
