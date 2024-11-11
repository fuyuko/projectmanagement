package net.fuyuko.projectmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path="/{id}")
    public @ResponseBody User getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping(path="/all")
    public @ResponseBody List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    

    @PostMapping(path="/add")
    public @ResponseBody String addUser(@RequestParam String name, @RequestParam(required = false) String description) {
        if(name == null){
            return "Name is required";
        }
        User user = new User();
        user.setName(name);
        if(description != null){
            user.setDescription(description);
        }
        userService.saveUser(user);
        return "Saved";
    }
    
    @PutMapping(path="/update")
    public @ResponseBody String updateUser(@RequestParam Integer id, @RequestParam(required = false) String name, @RequestParam(required = false) String description) {
        User user = userService.getUserById(id);
        if (user != null) {
            if (name != null) {
                user.setName(name);
            }
            if (description != null) {
                user.setDescription(description);
            }
            userService.updateUser(user);
            return "Updated";
        } else {
            return "User not found";
        }
    }


    @DeleteMapping(path="/{id}")
    public @ResponseBody String deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return "Deleted";
    }

    @DeleteMapping(path="/deleteAll")
    public @ResponseBody String deleteAllUsers() {
        userService.deleteAllUsers();
        return "Deleted all users.";
    }
}
