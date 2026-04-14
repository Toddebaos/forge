package com.forge.api.service;

import com.forge.api.dto.CommitMetricDTO;
import com.forge.api.model.postgres.CommitMetric;
import com.forge.api.repository.postgres.CommitMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitMetricService {
    
}
