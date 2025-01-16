package com.example.sapper.mapper;

import com.example.sapper.model.dto.GameDto;
import com.example.sapper.model.entity.Game;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface GameMapper {
    GameDto toDto(Game game);

    Game toEntity(GameDto gameDTO);
}
