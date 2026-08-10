package com.fiap.ec.backend_consultas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fiap.ec.backend_consultas.model.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
}