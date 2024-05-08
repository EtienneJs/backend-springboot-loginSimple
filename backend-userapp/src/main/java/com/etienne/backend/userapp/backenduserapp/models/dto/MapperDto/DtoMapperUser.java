package com.etienne.backend.userapp.backenduserapp.models.dto.MapperDto;

import com.etienne.backend.userapp.backenduserapp.models.dto.UserDto;
import com.etienne.backend.userapp.backenduserapp.models.entities.User;

public class DtoMapperUser {

    private DtoMapperUser(){

    }

    private User user;
    public static DtoMapperUser builder (){
        return new DtoMapperUser();
    }
    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDto build(){
        if(user == null){
            throw new RuntimeException("Debe pasar el entity user!");

        }
        return new UserDto(this.user.getId(), this.user.getUsername(),this.user.getEmail());
    }
}
