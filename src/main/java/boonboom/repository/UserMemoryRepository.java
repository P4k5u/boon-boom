package boonboom.repository;

import boonboom.model.UserMemory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMemoryRepository extends JpaRepository<UserMemory, Long> {
    
    Optional<UserMemory> findByUserIdAndKey(String userId, String key);
    
    List<UserMemory> findByUserId(String userId);
    
    List<UserMemory> findByUserIdOrderByUpdatedAtDesc(String userId);
    
    void deleteByUserIdAndKey(String userId, String key);
}