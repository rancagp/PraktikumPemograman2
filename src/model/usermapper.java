package model;

import java.util.List;
import org.apache.ibatis.annotations.*;

public interface usermapper {
    @Select("SELECT * FROM users")
    List<user> getAllUsers();

    @Insert("INSERT INTO users (name, email) VALUES (#{name}, #{email})")
    void insertUser(user user);
}