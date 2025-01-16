package com.example.sapper.mapper;

import com.example.sapper.model.dto.TurnDto;
import com.example.sapper.model.entity.Turn;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface TurnMapper {
    TurnDto toDto(Turn turn);

    Turn toEntity(TurnDto turnDto);
}
