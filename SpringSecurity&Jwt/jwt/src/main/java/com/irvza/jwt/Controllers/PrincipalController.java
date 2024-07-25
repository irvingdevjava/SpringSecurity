package com.irvza.jwt.Controllers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.irvza.jwt.Controllers.request.CreateUserDTO;
import com.irvza.jwt.Modelos.ERole;
import com.irvza.jwt.Modelos.RoleEntity;
import com.irvza.jwt.Modelos.UserEntity;
import com.irvza.jwt.Repositories.UserRepository;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1")
public class PrincipalController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO){

        Set<RoleEntity> roles=createUserDTO.getRoles().stream()
            .map(role -> RoleEntity.builder()
                    .name(ERole.valueOf(role))
                    .build())
            .collect(Collectors.toSet());

        UserEntity userEntity=UserEntity.builder()
                                .username(createUserDTO.getUsername())
                                .password(createUserDTO.getPassword())
                                .email(createUserDTO.getEmail())
                                .roles(roles)
                                .build();
                                

        userRepository.save(userEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);

    }


    @DeleteMapping("/user")
    public String deteleUser(@RequestParam String id){
            userRepository.deleteById(Long.parseLong(id));

        return "Usuario eliminado con el id:".concat(id);
    }

    
    

}
