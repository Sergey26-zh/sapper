package com.example.sapper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "cell")
public class Cell {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    private int row;

    @Column(nullable = false)
    private int col;

    @Column(nullable = false, length = 2)
    private String value;
}
