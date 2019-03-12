package com.jiaying.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jiaying.entity.UserEntity;

@Mapper
public interface MemberDao {
	
	@Select ("select id,username,password,phone,email,created,updated,openid from mb_user where id = #{userId}")
	UserEntity findByID(@Param("userId") Long userId);
	
	@Insert("INSERT INTO `mb_user` (username,password,phone,email,created,updated) VALUES (#{username}, #{password},#{phone},#{email},#{created},#{updated});")
	Integer insertUser(UserEntity userEntity);

	@Select ("select id,username,password,phone,email,created,updated,openid from mb_user where username = #{userName} and password = #{password}")
	UserEntity login(@Param("userName")String userName,@Param("password")String password);

	@Select ("select id,username,password,phone,email,created,updated,openid from mb_user where openid = #{openId}")
	UserEntity findByOpenIdUser(@Param("openId") String openId);
	
	@Update("update mb_user set openid = #{openId} where id = #{userId}")
	Integer updateByOpenIdUser(@Param("openId") String openId,@Param("userId") Integer userId);
}
