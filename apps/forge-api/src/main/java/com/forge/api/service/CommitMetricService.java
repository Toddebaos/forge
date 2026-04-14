package com.forge.api.service;

import com.forge.api.dto.CommitMetricDTO;
import com.forge.api.model.postgres.CommitMetric;
import com.forge.api.repository.postgres.CommitMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitMetricService {

    private final CommitMetricRepository metricRepository;

    public List<CommitMetricDTO> getMetricsByRepo(String owner, String repo) {
        String repoFullName = owner + "/" + repo;
        List<CommitMetric> metrics = metricRepository.findByRepoFullNameOrderByTimestampDesc(repoFullName);
        return metrics.stream()
            .map(this::toDTO)
            .toList();
    }

    private CommitMetricDTO toDTO(CommitMetric metric) {
        CommitMetricDTO dto = new CommitMetricDTO();
        dto.setId(metric.getId());
        dto.setAdditions(metric.getAdditions());
        dto.setAuthor(metric.getAuthor());
        dto.setCommitCount(metric.getCommitCount());
        dto.setDeletions(metric.getDeletions());
        dto.setRepoFullName(metric.getRepoFullName());
        dto.setTimeStamp((metric.getTimestamp()));

        return dto;
    }

}
    

