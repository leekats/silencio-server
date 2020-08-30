package com.silencio.server.bl;

import com.silencio.server.dal.UsersDAL;
import com.silencio.server.models.enums.Role;
import com.silencio.server.models.pojos.Auth;
import com.silencio.server.models.pojos.Login;
import com.silencio.server.models.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.silencio.server.models.enums.Role.UNAUTHORIZED;
import static java.util.Objects.nonNull;

@Service
public class UsersBL {
    private final UsersDAL dal;
    private final PersonsBL personsBL;

    @Autowired
    public UsersBL(UsersDAL dal, PersonsBL personsBL) {
        this.dal = dal;
        this.personsBL = personsBL;
    }

    public boolean addUser(User user) {
        if (nonNull(personsBL.getPerson(user.getPersonId()))) {
            return dal.addUser(user);
        }
        return false;
    }

    public boolean updatePassword(String username, String password) {
        return dal.updatePassword(username, password);
    }

    public void deleteUser(String id) {
        dal.deleteUser(id);
    }

    public Auth auth(Login login) {
        return Optional.ofNullable(dal.getUserByUsernameAndPassword(login.getUsername(), login.getPassword()))
                .map(u-> Auth.builder()
                        .personId(u.getPersonId())
                        .role(u.getRole()).build())
                .orElse(Auth.builder().personId(null).role(UNAUTHORIZED).build());
    }

    public boolean updateRole(String username, Role role) {
        return dal.updateRole(username, role);
    }

    public List<User> getAllUsers() {
        return dal.getAllUsers();
    }
}