package com.silencio.server.dal;

import com.silencio.server.dal.repositories.UsersRepository;
import com.silencio.server.models.enums.Role;
import com.silencio.server.models.pojos.Auth;
import com.silencio.server.models.pojos.Login;
import com.silencio.server.models.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersDAL {
    private final UsersRepository db;

    @Autowired
    public UsersDAL(UsersRepository db) {
        this.db = db;
    }

    public User getUserByUsername(String username) {
        return db.findUserByUsername(username);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return db.findUserByUsernameAndPassword(username, password);
    }

    public boolean addUser(User user) {
        Optional<User> maybeUser = Optional.ofNullable(getUserByUsername(user.getUsername()));
        if (!maybeUser.isPresent()) {
            db.save(user);
            return true;
        }

        return false;
    }

    public boolean updatePassword(String username, String password) {
        final Optional<User> maybeUser = Optional.ofNullable(db.findUserByUsername(username));
        return maybeUser.map(u -> {
            u.setPassword(password);
            db.save(u);
            return true;
        }).orElse(false);
    }

    public void deleteUser(String id) {
        db.deleteUserByUsername(id);
    }

    public boolean updateRole(String username, Role role) {
        return Optional.ofNullable(getUserByUsername(username))
                .map(u -> {
                    u.setRole(role);
                    db.save(u);
                    return true;
                })
                .orElse(false);

    }

    public List<User> getAllUsers() {
        return db.findAll();
    }
}
