package com.hireflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hireflow.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findBySkillNameIgnoreCase(String skillName);

    boolean existsBySkillNameIgnoreCase(String skillName);
}