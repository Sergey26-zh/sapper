package com.example.sapper.repository;

import com.example.sapper.entity.Cell;
import com.example.sapper.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CellRepository extends JpaRepository<Cell, UUID> {
}
