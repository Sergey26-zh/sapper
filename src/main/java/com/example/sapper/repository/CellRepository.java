package com.example.sapper.repository;

import com.example.sapper.model.entity.Cell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CellRepository extends JpaRepository<Cell, UUID> {
}
