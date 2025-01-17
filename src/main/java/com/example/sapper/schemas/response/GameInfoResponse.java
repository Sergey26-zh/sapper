package com.example.sapper.schemas.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class GameInfoResponse {
    private UUID game_id;
    private int width;
    private int height;
    private int mines_count;
    private boolean completed;
    private List<List<String>> field;
}
