package com.example.skillexchange.controller;

import com.example.skillexchange.model.Skill;
import com.example.skillexchange.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
@CrossOrigin(origins = "*")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping("/post")
    public ResponseEntity<Skill> postSkill(@RequestBody Skill skill) {
        return ResponseEntity.ok(skillService.postSkill(skill));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Skill>> searchSkill(@RequestParam String keyword) {
        return ResponseEntity.ok(skillService.searchSkill(keyword));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}