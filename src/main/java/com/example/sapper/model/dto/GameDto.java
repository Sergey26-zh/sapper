package com.example.sapper.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GameDto {
    private int width;
    private int height;
    private int minesCount;
    private boolean completed;
    private List<CellDto> cells;
}
