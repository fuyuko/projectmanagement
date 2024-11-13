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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebMvcTest(UserStoryController.class)
@ActiveProfiles("test")
public class UserStoryControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserStoryService userStoryService;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserStoryController userStoryController;

    // getUserStoryById Tests

    @Test
    public void testGetUserStoryById_UserStoryFound() throws Exception {
        UserStory userStory = new UserStory(1, 1, "I want to test", "So that I can verify");

        when(userStoryService.getUserStoryById(1)).thenReturn(userStory);

        mockMvc.perform(get("/userstory/1"))
                .andExpect(status().isOk());

        verify(userStoryService, times(1)).getUserStoryById(1);
    }

    @Test
    public void testGetUserStoryById_UserStoryNotFound() throws Exception {
        when(userStoryService.getUserStoryById(1)).thenReturn(null);

        mockMvc.perform(get("/userstory/1"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User story not found"));

        verify(userStoryService, times(1)).getUserStoryById(1);
    }

    //getUserStoriesByUserId Tests

    @Test
    public void testGetAllUserStoriesByUserId_Success() throws Exception {
        User user = new User(1, "John Doe", "A sample user");
        List<UserStory> userStories = new ArrayList<>(
            Arrays.asList(new UserStory(1, 1, "I want to test", "So that I can verify"),
                new UserStory(2, 1, "I want to test more", "So that I can verify more")));

        when(userService.getUserById(1)).thenReturn(user);
        when(userStoryService.getAllUserStoriesByUserId(1)).thenReturn(userStories);

        mockMvc.perform(get("/userstory/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(1);
        verify(userStoryService, times(1)).getAllUserStoriesByUserId(1);
    }

    @Test
    public void testGetAllUserStoriesByUserId_UserNotFound() throws Exception {
        when(userService.getUserById(1)).thenReturn(null);

        mockMvc.perform(get("/userstory/user/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1);
        verify(userStoryService, times(0)).getAllUserStoriesByUserId(1);
    }

    @Test
    public void testGetAllUserStoriesByUserId_Empty() throws Exception {
        User user = new User(1, "John Doe", "A sample user");

        when(userService.getUserById(1)).thenReturn(user);
        when(userStoryService.getAllUserStoriesByUserId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/userstory/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(1);
        verify(userStoryService, times(1)).getAllUserStoriesByUserId(1);
    }
    
    //getAllUsers Tests

    @Test
    public void testGetAllUserStories_Success() throws Exception {
        List<UserStory> userStories = new ArrayList<>(
            Arrays.asList(new UserStory(1, 1, "I want to test", "So that I can verify"),
                new UserStory(2, 1, "I want to test more", "So that I can verify more")));

        when(userStoryService.getAllUserStories()).thenReturn(userStories);

        mockMvc.perform(get("/userstory/all"))
                .andExpect(status().isOk());

        verify(userStoryService, times(1)).getAllUserStories();
    }

    @Test
    public void testGetAllUserStories_Empty() throws Exception {
        when(userStoryService.getAllUserStories()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/userstory/all"))
                .andExpect(status().isOk());

        verify(userStoryService, times(1)).getAllUserStories();
    }

    // addUserStory Tests

    @Test
    public void testAddUserStory_Success() throws Exception {
        User user = new User(1, "John Doe", "A sample user");
        UserStory userStory = new UserStory(1, 1, "I want to test", "So that I can verify");

        when(userService.getUserById(1)).thenReturn(user);
        when(userStoryService.saveUserStory(any(UserStory.class))).thenReturn(userStory);

        mockMvc.perform(post("/userstory/add")
                .param("userId", "1")
                .param("want", "I want to test")
                .param("soThat", "So that I can verify"))
                .andExpect(status().isOk())
                .andExpect(content().string("User story added."));

        verify(userService, times(1)).getUserById(1);
        verify(userStoryService, times(1)).saveUserStory(any(UserStory.class));
    }

    @Test
    public void testAddUserStory_UserNotFound() throws Exception {
        when(userService.getUserById(1)).thenReturn(null);

        mockMvc.perform(post("/userstory/add")
                .param("userId", "1")
                .param("want", "I want to test")
                .param("soThat", "So that I can verify"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1);
        verify(userStoryService, times(0)).saveUserStory(any(UserStory.class));
    }

    // updateUserStory Tests

    @Test
    public void testUpdateUserStory_Success() throws Exception {
        UserStory userStory = new UserStory(1, 1, "I want to test", "So that I can verify");
        UserStory updatedUserStory = new UserStory(1, 1, "I want to update", "So that I can verify");

        when(userStoryService.getUserStoryById(1)).thenReturn(userStory);
        when(userStoryService.saveUserStory(any(UserStory.class))).thenReturn(updatedUserStory);

        mockMvc.perform(put("/userstory/update")
                .param("id", "1")
                .param("want", "I want to update"))
                .andExpect(status().isOk())
                .andExpect(content().string("User story updated."));

        verify(userStoryService, times(1)).getUserStoryById(1);
        verify(userStoryService, times(1)).saveUserStory(any(UserStory.class));
    }

    @Test
    public void testUpdateUserStory_UserStoryNotFound() throws Exception {
        when(userStoryService.getUserStoryById(1)).thenReturn(null);

        mockMvc.perform(put("/userstory/update")
                .param("id", "1")
                .param("want", "I want to update"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User story not found"));

        verify(userStoryService, times(1)).getUserStoryById(1);
        verify(userStoryService, times(0)).saveUserStory(any(UserStory.class));
    }

    @Test
    public void testUpdateUserStory_Failed() throws Exception {
        UserStory userStory = new UserStory(1, 1, "I want to test", "So that I can verify");

        when(userStoryService.getUserStoryById(1)).thenReturn(userStory);
        when(userStoryService.saveUserStory(any(UserStory.class))).thenReturn(new UserStory());

        mockMvc.perform(put("/userstory/update")
                .param("id", "1")
                .param("want", "I want to update"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Failed to update user story"));

        verify(userStoryService, times(1)).getUserStoryById(1);
        verify(userStoryService, times(1)).saveUserStory(any(UserStory.class));
    }

    // deleteUserStoryById Tests

    @Test
    public void testDeleteUserStoryById_Success() throws Exception {
        UserStory userStory = new UserStory(1, 1, "I want to test", "So that I can verify");

        when(userStoryService.getUserStoryById(1)).thenReturn(userStory).thenReturn(null);

        mockMvc.perform(delete("/userstory/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User story deleted."));

        verify(userStoryService, times(2)).getUserStoryById(1);
        verify(userStoryService, times(1)).deleteUserStoryById(1);
    }

    @Test
    public void testDeleteUserStoryById_UserStoryNotFound() throws Exception {
        when(userStoryService.getUserStoryById(1)).thenReturn(null);

        mockMvc.perform(delete("/userstory/1"))
                .andExpect(status().isNotFound());

        verify(userStoryService, times(1)).getUserStoryById(1);
        verify(userStoryService, times(0)).deleteUserStoryById(1);
    }

    @Test
    public void testDeleteUserStoryById_Failed() throws Exception {
        UserStory userStory = new UserStory(1, 1, "I want to test", "So that I can verify");

        when(userStoryService.getUserStoryById(1)).thenReturn(userStory);

        mockMvc.perform(delete("/userstory/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Failed to delete user story"));

        verify(userStoryService, times(2)).getUserStoryById(1);
        verify(userStoryService, times(1)).deleteUserStoryById(1);
    }
    // deleteAllUserStories Tests

    @Test
    public void testDeleteAllUserStories_Success() throws Exception {
        when(userStoryService.getAllUserStories()).thenReturn(new ArrayList<>());

        mockMvc.perform(delete("/userstory/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("All user stories deleted."));

        verify(userStoryService, times(1)).deleteAllUserStories();
        verify(userStoryService, times(1)).getAllUserStories();
    }

    @Test
    public void testDeleteAllUserStories_Failed() throws Exception {
        List<UserStory> userStories = new ArrayList<>(
            Arrays.asList(new UserStory(1, 1, "I want to test", "So that I can verify"),
                new UserStory(2, 1, "I want to test more", "So that I can verify more")));

        when(userStoryService.getAllUserStories()).thenReturn(userStories);

        mockMvc.perform(delete("/userstory/all"))
                .andExpect(status().isInternalServerError())
                .andExpect(status().reason("Failed to delete all user stories"));

        verify(userStoryService, times(1)).deleteAllUserStories();
        verify(userStoryService, times(1)).getAllUserStories();
    }
}
