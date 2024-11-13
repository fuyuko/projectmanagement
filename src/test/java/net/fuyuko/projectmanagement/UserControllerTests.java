package net.fuyuko.projectmanagement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTests {
        
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserStoryService userStoryService;

    @InjectMocks
    private UserController userController;

    //getUserById Tests

    @Test
    public void testGetUserById_UserFound() throws Exception {
        User user = new User(1, "John Doe", "A sample user");

        when(userService.getUserById(1)).thenReturn(user);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(1);
    }

    @Test void testGetUserById_UserNotFound() throws Exception {
        when(userService.getUserById(1)).thenReturn(null);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1);
    }

    //getAllUsers Tests

    @Test
    public void testGetAllUsers_Success() throws Exception {
        List<User> users = new ArrayList<>(
            Arrays.asList(new User(1, "John Doe", "A sample user"),
                new User(2, "Jane Doe", "Another sample user"),
                new User(3, "Jack Doe", "Yet another sample user")));

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllUsers();
    }
    
    @Test
    public void testGetAllUsers_EmptyList() throws Exception { //expect to return an empty list with 200 status code
        List<User> users = Collections.emptyList();

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllUsers();
    }

    //addUser Tests

    @Test
    public void testAddUser_Success() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(new User(1, "John Doe", "A sample user"));

        mockMvc.perform(post("/user/add")
        .param("name", "John Doe")
        .param("description", "A sample user"))
        .andExpect(status().isOk())
        .andExpect(content().string("User added."));    

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testAddUser_missingDescription_Success() throws Exception {
            
        when(userService.saveUser(any(User.class))).thenReturn(new User(1, "John Doe", null));

        mockMvc.perform(post("/user/add")
                .param("name", "Jane Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("User added."));

        verify(userService, times(1)).saveUser(any(User.class));       
    }


    @Test
    public void testAddUser_MissingName() throws Exception {
        mockMvc.perform(post("/user/add")
                .param("description", "A sample user"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required parameter 'name' is not present.")); //the reason from requring 'name' parameter 

        verify(userService, times(0)).saveUser(any());
    }

    @Test
    public void testAddUser_failed() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(new User(null, "John Doe", "A sample user"));

        mockMvc.perform(post("/user/add")
        .param("name", "John Doe")
        .param("description", "A sample user"))
        .andExpect(status().isInternalServerError())
        .andExpect(status().reason("Failed to save user"));    

        verify(userService, times(1)).saveUser(any(User.class));
    }

    //updateUser Tests
    @Test
    public void testUpdateUser_Success() throws Exception {
        User user = new User(1, "John Doe", "A sample user");
        User updatedUser = new User(1, "Jane Doe", "Another sample user");

        when(userService.getUserById(1)).thenReturn(user);
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/update")
                .param("id", "1")
                .param("name", "Jane Doe")
                .param("description", "Another sample user"))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated"));

        verify(userService, times(3)).getUserById(1);
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testUpdateUser_UserNotFound() throws Exception { //invalid or missing user id
        when(userService.getUserById(1)).thenReturn(null);

        mockMvc.perform(put("/user/update")
                .param("id", "1")
                .param("name", "Jane Doe")
                .param("description", "Another sample user"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(0)).saveUser(any(User.class));
    }

    @Test
    public void testUpdateUser_MissingName() throws Exception {
        User user = new User(1, "John Doe", "A sample user");
        User updatedUser = new User(1, "John Doe", "Another sample user");

        when(userService.getUserById(1)).thenReturn(user);
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);
        mockMvc.perform(put("/user/update")
                .param("id", "1")
                .param("description", "Another sample user"))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated"));

        verify(userService, times(3)).getUserById(1);
        verify(userService, times(1)).saveUser(any(User.class));
    }   

    @Test
    public void testUpdateUser_MissingDescription() throws Exception {
        User user = new User(1, "John Doe", "A sample user");
        User updatedUser = new User(1, "Jane Doe", "A sample user");

        when(userService.getUserById(1)).thenReturn(user);
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/update")
                .param("id", "1")
                .param("name", "Jane Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated"));

        verify(userService, times(3)).getUserById(1);
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testUpdateUser_failedName() throws Exception {
        User user = new User(1, "John Doe", "A sample user");
        User updatedUser = new User(1, "Jane Doe", "Another sample user");

        when(userService.getUserById(1)).thenReturn(user);
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/update")
                .param("id", "1")
                .param("name", "John Doe")
                .param("description", "Another sample user"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Failed to update user"));

        verify(userService, times(2)).getUserById(1);
        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void testUpdateUser_failedDescription() throws Exception {
        User user = new User(1, "John Doe", "A sample user");
        User updatedUser = new User(1, "Jane Doe", "Another sample user");

        when(userService.getUserById(1)).thenReturn(user);
        when(userService.saveUser(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/update")
                .param("id", "1")
                .param("name", "Jane Doe")
                .param("description", "A sample user"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Failed to update user"));

        verify(userService, times(3)).getUserById(1);
        verify(userService, times(1)).saveUser(any(User.class));
    }

    //deleteUserById Tests
    @Test
    public void testDeleteUserById_Success() throws Exception {
        User user = new User(1, "John Doe", "A sample user");

        when(userService.getUserById(1)).thenReturn(user, (User) null);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("The user is deleted."));

        verify(userService, times(2)).getUserById(1);
        verify(userService, times(1)).deleteUserById(1);
    }

    @Test
    public void testDeleteUserById_UserNotFound() throws Exception {
        when(userService.getUserById(1)).thenReturn(null);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User not found"));

        verify(userService, times(1)).getUserById(1);
        verify(userService, times(0)).deleteUserById(1);
    }

    @Test
    public void testDeleteUserById_UserStoryExist() throws Exception {
        User user = new User(1, "John Doe", "A sample user");
        List<UserStory> userStories = new ArrayList<>(
            Arrays.asList(new UserStory(1, 1, "I want to do something", "So that I can achieve something"),
                new UserStory(2, 1, "I want to do something else", "So that I can achieve something else")));

        when(userService.getUserById(1)).thenReturn(user);
        when(userStoryService.getAllUserStoriesByUserId(1)).thenReturn(userStories);

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Need to delete user stories associated with the user first."));

        verify(userService, times(1)).getUserById(1);
        verify(userStoryService, times(1)).getAllUserStoriesByUserId(1);
        verify(userService, times(0)).deleteUserById(1);
    }

    @Test
    public void testDeleteUserById_failed() throws Exception {
        User user = new User(1, "John Doe", "A sample user");


        when(userService.getUserById(1)).thenReturn(user);
        when(userStoryService.getAllUserStoriesByUserId(1)).thenReturn(new ArrayList<>());

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Failed to delete user"));

        verify(userService, times(2)).getUserById(1);
        verify(userStoryService, times(1)).getAllUserStoriesByUserId(1);    
        verify(userService, times(1)).deleteUserById(1);
    }
 
    //deleteAllUsers Tests

    @Test
    public void testDeleteAllUsers_Success() throws Exception {

        when(userStoryService.getAllUserStories()).thenReturn(new ArrayList<>());
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        

        mockMvc.perform(delete("/user/deleteAll"))
                .andExpect(status().isOk())
                .andExpect(content().string("All users are deleted."));
        
        verify(userStoryService, times(1)).getAllUserStories();
        verify(userService, times(1)).deleteAllUsers();
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testDeleteAllUsers_UserStoryExist() throws Exception {
        List<UserStory> userStories = new ArrayList<>(
            Arrays.asList(new UserStory(1, 1, "I want to do something", "So that I can achieve something"),
                new UserStory(2, 1, "I want to do something else", "So that I can achieve something else")));
        when(userStoryService.getAllUserStories()).thenReturn(userStories);

        mockMvc.perform(delete("/user/deleteAll"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Need to delete all user stories first."));

        verify(userStoryService, times(1)).getAllUserStories();
        verify(userService, times(0)).deleteAllUsers();
        verify(userService, times(0)).getAllUsers();
    }

    @Test
    public void testDeleteAllUsers_failed() throws Exception {
        List<User> users = new ArrayList<>(
            Arrays.asList(new User(1, "John Doe", "A sample user"),
                new User(2, "Jane Doe", "Another sample user"),
                new User(3, "Jack Doe", "Yet another sample user")));

        when(userStoryService.getAllUserStories()).thenReturn(new ArrayList<>());
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(delete("/user/deleteAll"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Failed to delete all users"));

        verify(userStoryService, times(1)).getAllUserStories();
        verify(userService, times(1)).deleteAllUsers();
        verify(userService, times(1)).getAllUsers();
    }

}
