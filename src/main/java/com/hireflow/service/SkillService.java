package com.hireflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hireflow.entity.Skill;
import com.hireflow.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    public Optional<Skill> getSkillByName(String skillName) {
        return skillRepository.findBySkillNameIgnoreCase(skillName);
    }

    public boolean skillExists(String skillName) {
        return skillRepository.existsBySkillNameIgnoreCase(skillName);
    }

    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }
}