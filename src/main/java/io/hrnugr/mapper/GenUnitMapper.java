package io.hrnugr.mapper;

import io.hrnugr.dto.GenUnitDto;
import io.hrnugr.entity.GenUnit;

public class GenUnitMapper {

    public GenUnit mapToEntity(GenUnitDto unitDto) {
        return GenUnit.builder()
                .unitId(unitDto.getUnitId())
                .build();
    }
}
