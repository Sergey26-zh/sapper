package com.example.sapper.repository;

import com.example.sapper.model.entity.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TurnRepository extends JpaRepository<Turn, UUID> {
}
