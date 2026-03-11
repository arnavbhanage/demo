package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    // No need to declare anything — save(), findById(), findAll() etc. are all inherited
}