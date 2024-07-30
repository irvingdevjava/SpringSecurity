package com.irvza.jwt;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.irvza.jwt.Modelos.ERole;
import com.irvza.jwt.Modelos.RoleEntity;
import com.irvza.jwt.Modelos.UserEntity;
import com.irvza.jwt.Repositories.UserRepository;

@SpringBootApplication
public class JwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepository;


	@Bean
	public CommandLineRunner init() {
		return args -> {
			UserEntity userEntity = UserEntity.builder()
				.email("irving@gmail.com")
				.username("irving")
				.password(passwordEncoder.encode("1234"))
				.roles(Set.of(RoleEntity.builder()
					.name(ERole.ADMIN)
					.build()))
				.build();

			UserEntity userEntity1 = UserEntity.builder()
				.email("jose@gmail.com")
				.username("jose")
				.password(passwordEncoder.encode("1234"))
				.roles(Set.of(RoleEntity.builder()
					.name(ERole.USER)
					.build()))
				.build();

			UserEntity userEntity2 = UserEntity.builder()
				.email("rick@gmail.com")
				.username("rick")
				.password(passwordEncoder.encode("1234"))
				.roles(Set.of(RoleEntity.builder()
					.name(ERole.INVITED)
					.build()))
				.build();

			userRepository.save(userEntity);
			userRepository.save(userEntity1);
			userRepository.save(userEntity2);
		};
	}

}
