package com.vetcontrol.vetcontrolbackend.repository;

import com.vetcontrol.vetcontrolbackend.entity.Vacuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacunaRepository extends JpaRepository<Vacuna, Long> {
    List<Vacuna> findByMascotaId(Long mascotaId);
}
