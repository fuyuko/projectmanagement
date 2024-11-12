package net.fuyuko.projectmanagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStoryService {
    @Autowired
    private UserStoryRepository userStoryRepository;

    public UserStoryService(UserStoryRepository userStoryRepository) {
        this.userStoryRepository = userStoryRepository;
    }

    public UserStoryRepository getUserStoryRepository() {
        return userStoryRepository;
    }

    public void setUserStoryRepository(UserStoryRepository userStoryRepository) {
        this.userStoryRepository = userStoryRepository;
    }

    public UserStory saveUserStory(UserStory userStory) {
        return userStoryRepository.save(userStory);
    }

    public void deleteUserStoryById(Integer id) {
        userStoryRepository.deleteById(id);
    }

    public UserStory getUserStoryById(Integer id) {
        return userStoryRepository.findById(id).orElse(null);
    }

    public List<UserStory> getAllUserStories() {
        return userStoryRepository.findAll();
    }

    public void deleteAllUserStories() {
        userStoryRepository.deleteAll();
    }
    
}
