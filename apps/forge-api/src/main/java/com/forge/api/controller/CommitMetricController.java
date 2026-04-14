package com.forge.api.controller;

import com.forge.api.dto.CommitMetricDTO;
import com.forge.api.service.CommitMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/repos/{owner}/{repo}/commits")
@RequiredArgsConstructor
public class CommitMetricController {

    private final CommitMetricService metricService;

    @GetMapping
    public ResponseEntity<List<CommitMetricDTO>> getMetricsByRepo(
        @PathVariable String owner, 
        @PathVariable String repo) {
        return ResponseEntity.ok(metricService.getMetricsByRepo(owner, repo));
    }

    
}
