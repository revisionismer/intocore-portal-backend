package com.intocore.user.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.intocore.user.domain.User;

@Mapper
public interface UserMapper {

	 @Select("SELECT username FROM test_user WHERE id = #{id}")
	 String findUsernameById1(Long id);
	 
	 @Select("SELECT username FROM user_tb WHERE id = #{id}")
	 String findUsernameById2(Long id);
	 
	 @Insert("INSERT INTO user_tb(id, username, password, name, role, phone, createdDate) VALUES (#{id}, #{username}, #{password}, #{name}, #{role}, #{phone}, NOW())")
	 void insertTestUser(User user);
	 
}
