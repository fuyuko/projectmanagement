package net.fuyuko.projectmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import net.fuyuko.projectmanagement.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}

