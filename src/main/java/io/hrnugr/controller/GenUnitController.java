package io.hrnugr.controller;

import io.hrnugr.entity.GenUnit;
import io.hrnugr.service.GenUnitService;
import io.hrnugr.util.Endpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(Endpoints.ROOT_GEN_UNIT)
public class GenUnitController {

    @Autowired
    GenUnitService genUnitService;

    @GetMapping(Endpoints.GET_GEN_UNIT_BY_ID)
    public ResponseEntity<GenUnit> getUnit(@PathVariable String unitId){
        log.info("Request to get group: {}", unitId);
        GenUnit result = genUnitService.genUnit(unitId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(Endpoints.CREATE_GEN_UNIT)
    public ResponseEntity<?> create(@Valid @RequestBody GenUnit genUnit) throws URISyntaxException {
        log.info("Request to create genUnit: {}", genUnit);
        GenUnit result = genUnitService.create(genUnit);
        return ResponseEntity.created(new URI(Endpoints.ROOT_GEN_UNIT + result.getUnitId()))
                .body(result);
    }

    @PutMapping(Endpoints.UPDATE_GEN_UNIT)
    public ResponseEntity<?> update(@Valid @RequestBody GenUnit genUnit){
        log.info("Request to update genUnit: {}", genUnit);
        GenUnit result = genUnitService.create(genUnit);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping(Endpoints.DELETE_GEN_UNIT)
    public ResponseEntity<?> deleteGroup(@PathVariable String unitId) {
        log.info("Request to delete group: {}", unitId);
        genUnitService.deleteById(unitId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(Endpoints.GET_ALL_GEN_UNIT)
    public ResponseEntity<?> getAll() {
        List<GenUnit> list = genUnitService.getAll();
        return new ResponseEntity(list, HttpStatus.OK);
    }

}
