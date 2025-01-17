package com.example.sapper.schemas.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Информация об игре
 */
@Data
public class GameInfoResponse {
    private UUID gameId;
    private int width;
    private int height;
    private int minesCount;
    private boolean completed;
    private List<List<String>> field;
}
