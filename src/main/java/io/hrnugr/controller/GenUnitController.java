package io.hrnugr.controller;

import io.hrnugr.entity.GenUnit;
import io.hrnugr.service.GenUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/genunit")
public class GenUnitController {

    @Autowired
    GenUnitService genUnitService;

    @GetMapping("/{unitId}")
    public ResponseEntity<GenUnit> getUnit(@PathVariable String unitId){
        log.info("Request to get group: {}", unitId);
        GenUnit result = genUnitService.genUnit(unitId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody GenUnit genUnit) throws URISyntaxException {
        log.info("Request to create genUnit: {}", genUnit);
        GenUnit result = genUnitService.create(genUnit);
        return ResponseEntity.created(new URI("/genunit/" + result.getUnitId()))
                .body(result);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody GenUnit genUnit){
        log.info("Request to update genUnit: {}", genUnit);
        GenUnit result = genUnitService.create(genUnit);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{genUnitId}")
    public ResponseEntity<?> deleteGroup(@PathVariable String genUnitId) {
        log.info("Request to delete group: {}", genUnitId);
        genUnitService.deleteById(genUnitId);
        return ResponseEntity.ok().build();
    }

}
