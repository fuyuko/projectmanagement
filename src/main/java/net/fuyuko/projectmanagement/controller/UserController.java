package net.fuyuko.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.fuyuko.projectmanagement.entity.User;
import net.fuyuko.projectmanagement.service.UserService;

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path="/add")
    public @ResponseBody String addUser(@RequestParam String name, @RequestParam(required = false) String description) {
        User user = new User();
        user.setName(name);
        if(description != null){
            user.setDescription(description);
        }
        userService.saveUser(user);
        return "Saved";
    }

    @GetMapping(path="/{id}")
    public @ResponseBody User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.getAllUsers();
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
}
