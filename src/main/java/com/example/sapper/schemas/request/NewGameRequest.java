package com.example.sapper.schemas.request;

import lombok.Data;

/**
 * Настройки новой игры
 */
@Data
public class NewGameRequest {
    private int width;
    private int height;
    private int minesCount;
}