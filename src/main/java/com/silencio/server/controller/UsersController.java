package com.silencio.server.controller;

import com.silencio.server.bl.UsersBL;
import com.silencio.server.models.enums.Role;
import com.silencio.server.models.pojos.Auth;
import com.silencio.server.models.pojos.Login;
import com.silencio.server.models.pojos.PasswordChange;
import com.silencio.server.models.pojos.RoleUpdate;
import com.silencio.server.models.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/users")
@RestController
public class UsersController {
    private final UsersBL bl;

    @Autowired
    public UsersController(UsersBL usersBL) {
        this.bl = usersBL;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addUser(@Valid @RequestBody User user) {
        return bl.addUser(user);
    }

    @PutMapping(path = "/password/{username}")
    public boolean updatePassword(@PathVariable("username") String username, @RequestBody PasswordChange password) {
        return bl.updatePassword(username, password.getPassword());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return bl.getAllUsers();
    }

    @PutMapping(path = "/role/{username}")
    public boolean updateRole(@PathVariable("username") String username, @RequestBody RoleUpdate role) {
        return bl.updateRole(username, role.getRole());
    }

    @DeleteMapping(path = "/{username}")
    public void deleteUser(@PathVariable("username") String username) {
        bl.deleteUser(username);
    }

    @PostMapping(path = "/auth")
    public Auth auth(@Valid @RequestBody Login login) {
        return bl.auth(login);
    }
}
