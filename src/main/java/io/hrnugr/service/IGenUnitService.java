package io.hrnugr.service;

import io.hrnugr.entity.GenUnit;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface IGenUnitService {

    GenUnit genUnit(@NotNull String unitId);
    GenUnit create(@NotNull @Valid GenUnit genUnit);
    void deleteById(@NotNull String unitId);
    List<GenUnit> getAll();
}
