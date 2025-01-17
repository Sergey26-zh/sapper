package com.example.sapper.schemas.request;

import lombok.Data;

import java.util.UUID;

/**
 * Данные о ходе игрока
 */
@Data
public class GameTurnRequest {
    private UUID gameId;
    private int col;
    private int row;
}