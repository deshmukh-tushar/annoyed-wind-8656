package com.masai;

import java.util.*;
import java.util.stream.Collectors;

public class LoginServiceImpl implements LoginService {

    private static final Set<UserEntity> users = new HashSet<>();
    static{
        AdministratorEntity administratorEntity = new AdministratorEntity();
        administratorEntity.setEmail("admin@gmail.com");
        administratorEntity.setPassword("12345");
        users.add(administratorEntity);
    }
   
    @Override
    public UserEntity login(String email, String password) {
        if(users.isEmpty()){
            return null;
        }
        Optional<UserEntity> userEntityOptional = find(email);
        if(userEntityOptional.isPresent() &&  userEntityOptional.get().getPassword().equals(password)){
           return userEntityOptional.get();
        }
        return null;
    }

    Optional<UserEntity> find(String email){
        return users.stream()
                .filter(userEntity -> !userEntity.getDeleted())
                .filter(user ->
                        user.getEmail().equals(email))
                .findAny();
    }

    @Override
    public boolean register(UserEntity userEntity) {
        if(!users.contains(userEntity)){
            users.add(userEntity);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAccount(String email) {
        users.forEach(userEntity ->
        {
            if(userEntity.getEmail().equals(email)){
                userEntity.setDeleted(Boolean.TRUE);
            }
        });
        return false;
    }

    @Override
    public List<UserEntity> getAllConsumers() {
        return users.stream().filter(userEntity -> userEntity instanceof ConsumerEntity).filter(userEntity -> !userEntity.getDeleted()).collect(Collectors.toList());
    }

    @Override
    public UserEntity findByEmail(String email) {
        return users.stream()
                .filter(userEntity -> userEntity instanceof ConsumerEntity)
                .filter(userEntity -> userEntity.getEmail().equals(email))
                .findFirst().orElse(null);
    }

	
	
	
	
}
