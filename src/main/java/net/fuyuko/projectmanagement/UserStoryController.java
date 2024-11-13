package net.fuyuko.projectmanagement;

import java.util.List;

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

@Controller
@RequestMapping(path="/userstory")
public class UserStoryController {
    @Autowired
    private UserStoryService userStoryService;

    @Autowired
    private UserService userService;

    @GetMapping(path="/{id}")
    public @ResponseBody UserStory getUserStoryById(@PathVariable Integer id) {
        UserStory userStory = userStoryService.getUserStoryById(id);
        if (userStory != null) {
            return userStory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User story not found");
        }
    }

    /* 

    @GetMapping(path="/user/{userId}")
    public @ResponseBody List<UserStory> getAllUserStoriesByUserId(@PathVariable Integer userId) {
        //check if user exists
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return userStoryService.getAllUserStoriesByUserId(userId);
    }

     */

    @GetMapping(path="/all")
    public @ResponseBody List<UserStory> getAllUserStories() {
        return userStoryService.getAllUserStories();
    }

   

    @PostMapping(path = "/add") 
    public @ResponseBody String addUserStory(@RequestParam Integer userId, @RequestParam String want, @RequestParam String soThat) {
        
        //check if user exists
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        UserStory userStory = new UserStory();
        userStory.setUserId(userId);
        userStory.setWant(want);
        userStory.setSoThat(soThat);

        UserStory savedUserStory = userStoryService.saveUserStory(userStory);

        //check if user story is saved
        if (savedUserStory.getId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save user story");
        }

        return "User story added.";
    }

    @PutMapping(path = "/update")
    public @ResponseBody String updateUserStory(@RequestParam Integer id, @RequestParam(required = false) Integer userId, @RequestParam(required = false) String want, @RequestParam(required = false) String soThat) {
        UserStory userStory = userStoryService.getUserStoryById(id);
        if (userStory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User story not found");
        }

        if (userId != null) {
            //check if user exists
            User user = userService.getUserById(userId);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            userStory.setUserId(userId);
        }
        if (want != null) {
            userStory.setWant(want);
        }
        if (soThat != null) {
            userStory.setSoThat(soThat);
        }

        UserStory updatedUserStory = userStoryService.saveUserStory(userStory);

        //check if user story is saved
        if (updatedUserStory.getId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update user story");
        }

        return "User story updated.";
    }

    @DeleteMapping(path="/{id}")
    public @ResponseBody String deleteUserStoryById(@PathVariable Integer id) {
        UserStory userStory = userStoryService.getUserStoryById(id);
        if (userStory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User story not found");
        }

        userStoryService.deleteUserStoryById(id);

        //check if user story is deleted
        if (userStoryService.getUserStoryById(id) != null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete user story");
        }

        return "User story deleted.";
    }

    @DeleteMapping(path="/all")
    public @ResponseBody String deleteAllUserStories() {
        userStoryService.deleteAllUserStories();

        //check if all user stories are deleted
        if (userStoryService.getAllUserStories().size() != 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete all user stories");
        }

        return "All user stories deleted.";
    }
    
}
