package net.fuyuko.projectmanagement.repository;

import org.springframework.data.repository.CrudRepository;

import net.fuyuko.projectmanagement.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}

