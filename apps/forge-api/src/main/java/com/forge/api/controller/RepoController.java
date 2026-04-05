package com.forge.api.controller;

import com.forge.api.dto.RepoDTO;
import com.forge.api.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RepoController {

    private final RepoService repoService;

    @GetMapping
    public ResponseEntity<List<RepoDTO>> getAllRepos() {
        return ResponseEntity.ok(repoService.getAllRepos());
    }

    @GetMapping("/{owner}/{repo}")
    public ResponseEntity<RepoDTO> getRepo(
            @PathVariable String owner,
            @PathVariable String repo) {
        return ResponseEntity.ok(repoService.getRepoByFullName(owner, repo));
    }

    @GetMapping("/by-language")
    public ResponseEntity<List<RepoDTO>> getByLanguage(
            @RequestParam String language) {
        return ResponseEntity.ok(repoService.getReposByLanguage(language));
    }
}