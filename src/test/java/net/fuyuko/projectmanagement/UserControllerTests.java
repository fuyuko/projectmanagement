package net.fuyuko.projectmanagement;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
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
                .param("name", "John Doe"))
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
}
