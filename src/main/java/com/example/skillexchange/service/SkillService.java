package com.example.skillexchange.service;

import com.example.skillexchange.model.Skill;
import com.example.skillexchange.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    public Skill postSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public List<Skill> searchSkill(String keyword) {
        return skillRepository.findBySkillNameContaining(keyword);
    }

    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }
}