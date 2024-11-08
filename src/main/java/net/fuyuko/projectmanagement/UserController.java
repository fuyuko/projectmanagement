package net.fuyuko.projectmanagement;

import java.util.Optional;

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

@Controller
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam String name, @RequestParam String description) {
        User n = new User();
        n.setName(name);
        n.setDescription(description);
        userRepository.save(n);
        return "Saved";
    } 

    @GetMapping(path="/{id}")
    public @ResponseBody Optional<User> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @PutMapping(path="/update")
    public @ResponseBody String updateUser(@RequestParam Integer id, @RequestParam String name, @RequestParam String description) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(name);
            user.setDescription(description);
            userRepository.save(user);
            return "Updated";
        } else {
            return "User not found";
        }
    }

    @DeleteMapping(path="/{id}")
    public @ResponseBody String deleteUserById(@PathVariable Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return "Deleted";
        } else {
            return "User not found";
        }
    }
}
