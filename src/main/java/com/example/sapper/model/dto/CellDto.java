package com.example.sapper.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CellDto {
    private UUID gameId;
    private int row;
    private int col;

}
