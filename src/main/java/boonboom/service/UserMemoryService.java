package boonboom.service;

import boonboom.model.UserMemory;
import boonboom.repository.UserMemoryRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMemoryService {
    
    private final UserMemoryRepository userMemoryRepository;
    private static final String DEFAULT_USER_ID = "default_user";
    
    public UserMemoryService(UserMemoryRepository userMemoryRepository) {
        this.userMemoryRepository = userMemoryRepository;
    }
    
    @Tool(description = "Remember important information about the user (preferences, goals, portfolio, etc.)")
    public String rememberUserInfo(String key, String value, String userId) {
        String actualUserId = userId != null ? userId : DEFAULT_USER_ID;
        
        try {
            var existingMemory = userMemoryRepository.findByUserIdAndKey(actualUserId, key);
            UserMemory memory;
            
            if (existingMemory.isPresent()) {
                memory = existingMemory.get();
                memory.setValue(value);
            } else {
                memory = new UserMemory(actualUserId, key, value);
            }
            
            userMemoryRepository.save(memory);
            return "✓ Remembered: " + key + " = " + value;
            
        } catch (Exception e) {
            return "Error saving memory: " + e.getMessage();
        }
    }
    
    @Tool(description = "Recall specific information about the user")
    public String recallUserInfo(String key, String userId) {
        String actualUserId = userId != null ? userId : DEFAULT_USER_ID;
        
        try {
            var memory = userMemoryRepository.findByUserIdAndKey(actualUserId, key);
            if (memory.isPresent()) {
                return "I remember: " + key + " = " + memory.get().getValue();
            } else {
                return "I don't have any information stored for: " + key;
            }
        } catch (Exception e) {
            return "Error retrieving memory: " + e.getMessage();
        }
    }
    
    @Tool(description = "Get all remembered information about the user")
    public String getAllUserMemories(String userId) {
        String actualUserId = userId != null ? userId : DEFAULT_USER_ID;
        
        try {
            List<UserMemory> memories = userMemoryRepository.findByUserIdOrderByUpdatedAtDesc(actualUserId);
            if (memories.isEmpty()) {
                return "I don't have any stored memories for this user yet.";
            }
            
            StringBuilder result = new StringBuilder("Here's what I remember about you:\\n\\n");
            for (UserMemory memory : memories) {
                result.append("• ").append(memory.getKey())
                      .append(": ").append(memory.getValue())
                      .append(" (updated: ").append(memory.getUpdatedAt().toLocalDate())
                      .append(")\\n");
            }
            
            return result.toString();
        } catch (Exception e) {
            return "Error retrieving memories: " + e.getMessage();
        }
    }
    
    @Tool(description = "Forget specific information about the user")
    @Transactional
    public String forgetUserInfo(String key, String userId) {
        String actualUserId = userId != null ? userId : DEFAULT_USER_ID;
        
        try {
            var memory = userMemoryRepository.findByUserIdAndKey(actualUserId, key);
            if (memory.isPresent()) {
                userMemoryRepository.deleteByUserIdAndKey(actualUserId, key);
                return "✓ Forgot: " + key;
            } else {
                return "I don't have any information stored for: " + key;
            }
        } catch (Exception e) {
            return "Error forgetting memory: " + e.getMessage();
        }
    }
}