package com.silencio.server.dal.repositories;

import com.silencio.server.models.pojos.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {
    void deleteUserByUsername(String username);
    User findUserByUsername(String username);
    User findUserByUsernameAndPassword(String username, String password);
}
