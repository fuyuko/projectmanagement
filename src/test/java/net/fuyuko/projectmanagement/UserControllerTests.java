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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @InjectMocks
    private UserController userController;


    @Test
    public void testAddUser_Success() throws Exception {
        mockMvc.perform(post("/user/add")
                .param("name", "John Doe")
                .param("description", "A sample user"))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved"));

        verify(userService, times(1)).saveUser(any(User.class));       
    }

    @Test
    public void testAddUser_missingDescription_Success() throws Exception {
        mockMvc.perform(post("/user/add")
                .param("name", "Jane Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved"));

        verify(userService, times(1)).saveUser(any(User.class));       
    }

    @Test
    public void testAddUser_MissingName() throws Exception {
        mockMvc.perform(post("/user/add")
                .param("description", "A sample user"))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).saveUser(any(User.class));
    }

    @Test
    public void testGetAllUsers() throws Exception {
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


}
