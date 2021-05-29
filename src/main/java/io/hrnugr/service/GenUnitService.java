package io.hrnugr.service;

import io.hrnugr.cache.GenUnitCache;
import io.hrnugr.config.RedisClient;
import io.hrnugr.entity.GenUnit;
import io.hrnugr.repository.GenUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GenUnitService {

    RedisClient redisClient = RedisClient.getInstance();
    GenUnitCache genUnitCache = GenUnitCache.getInstance();

    @Autowired
    GenUnitRepository genUnitRepository;

    /**
     * @param unitId
     * @return GenUnit by unitId
     */
    public GenUnit genUnit(String unitId){
        GenUnit genUnit = null;
        if (redisClient.isConnected()){
            genUnit = genUnitCache.getUnit(unitId);
            if (genUnit == null){
               genUnit = genUnitRepository.findById(unitId).orElse(null);
               if (genUnit != null){
                   genUnitCache.save(Collections.singletonList(genUnit));
               }
            }
        }else{
            genUnit = genUnitRepository.findById(unitId).orElse(null);
        }

        return genUnit;
    }

    /**
     * @param genUnit
     * @return GenUnit Create new GenUnit or update GenUnit by unitId
     */
    public GenUnit create(GenUnit genUnit) {
        GenUnit genUnitDb = genUnitRepository.save(genUnit);
        if (redisClient.isConnected()){
            genUnitCache.save(Collections.singletonList(genUnit));
        }
        return genUnitDb;
    }


    /**
     * @param unitId
     * Delete GenUnit by unitId
     */
    public void deleteById(String unitId) {
        GenUnit genUnit = genUnitRepository.findById(unitId).orElse(null);
        if (genUnit != null){
            genUnitRepository.deleteById(unitId);
        }
        if (redisClient.isConnected()){
            genUnitCache.delete(unitId);
        }
    }

    public List<GenUnit> getAll() {
        List<GenUnit> unitList;
        if (redisClient.isConnected()){
            unitList = genUnitCache.getAllUnit();
            if (unitList.size() == 0){
                unitList = genUnitRepository.findAll();
                if (unitList.size() > 0){
                    genUnitCache.save(unitList);
                }
            }
        }else{
            unitList = genUnitRepository.findAll();
        }
        return unitList;
    }

}
