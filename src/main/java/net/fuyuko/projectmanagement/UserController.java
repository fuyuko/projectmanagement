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
    

    @PostMapping(path = "/add")
    public @ResponseBody String addUser(@RequestParam String name, @RequestParam(required = false) String description) {

        User user = new User();
        user.setName(name);
        if(description != null){
            user.setDescription(description);
        }

       User savedUser = userService.saveUser(user);

        //check if user is saved
       if (savedUser.getId() == null) {
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save user");
       }

        return "User added.";
    }
    
    @PutMapping(path="/update")
    public @ResponseBody String updateUser(@RequestParam Integer id, @RequestParam(required = false) String name, @RequestParam(required = false) String description) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }else{
            if (name != null) {
                user.setName(name);
            }
            if (description != null) {
                user.setDescription(description);
            }
            User savedUser = userService.saveUser(user);

            //check if user is updated
            if (userService.getUserById(id).getName() != savedUser.getName() || userService.getUserById(id).getDescription() != savedUser.getDescription()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user");
            }

            return "Updated";
        } 
    }


    @DeleteMapping(path="/{id}")
    public @ResponseBody String deleteUserById(@PathVariable Integer id) {
        // Check if user exists
        User user = userService.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userService.deleteUserById(id);

        // Check if user is deleted
        if (userService.getUserById(id) != null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user");
        }

        return "The user is deleted.";
    }

    @DeleteMapping(path="/deleteAll")
    public @ResponseBody String deleteAllUsers() {
        userService.deleteAllUsers();

        //chek if all users are deleted
        if (userService.getAllUsers().size() > 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete all users");
        }

        return "Deleted all users.";
    }
}
