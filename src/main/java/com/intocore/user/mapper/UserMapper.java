package com.intocore.user.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.intocore.user.domain.User;

@Mapper
public interface UserMapper {

	 @Select("SELECT username FROM test_user WHERE id = #{id}")
	 String findUsernameById1(Long id);
	 
	 @Select("SELECT username FROM users WHERE id = #{id}")
	 String findUsernameById2(Long id);
	 
	 @Insert("INSERT INTO users (id, username, password, role) VALUES (#{id}, #{username}, #{password}, #{role})")
	 void insertTestUser(User user);
	 
	 
}
