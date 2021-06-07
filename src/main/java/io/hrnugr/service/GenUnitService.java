package io.hrnugr.service;

import io.hrnugr.cache.GenUnitCache;
import io.hrnugr.config.RedisClient;
import io.hrnugr.entity.GenUnit;
import io.hrnugr.repository.GenUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true) //Read-only transaction have better performance.
public class GenUnitService implements IGenUnitService{

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
    @Transactional
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
    @Transactional
    public void deleteById(String unitId) {
        var genUnit = genUnitRepository.findById(unitId).orElse(null);
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
