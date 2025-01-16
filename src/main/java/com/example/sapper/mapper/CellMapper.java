package com.example.sapper.mapper;

import com.example.sapper.model.dto.CellDto;
import com.example.sapper.model.entity.Cell;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CellMapper {
    CellDto toDto(Cell cell);

    Cell toEntity(CellDto cellDto);
}
