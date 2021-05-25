package io.hrnugr.repository;

import io.hrnugr.entity.GenUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenUnitRepository extends JpaRepository<GenUnit,String> {
}
