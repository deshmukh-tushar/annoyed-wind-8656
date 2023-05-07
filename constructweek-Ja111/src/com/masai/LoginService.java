package com.masai;

import java.util.List;
//import com.masai.UserEntity;
public interface LoginService {

	 UserEntity login(String userName, String password);
	    boolean register(UserEntity userEntity);

	    boolean deleteAccount(String userName);

	    List<UserEntity> getAllConsumers();

	    UserEntity findByEmail(String email);
	
}
