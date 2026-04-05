package com.forge.ingestor.scheduler;

import com.forge.ingestor.service.IngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class IngestionScheduler {

    private final IngestionService ingestionService;

    // fixedDelayString — väntar tills förra körningen är klar innan nästa startar
    // Undviker att två ingestions körs parallellt om GitHub är långsamt
    @Scheduled(fixedDelayString = "${github.poll.interval}")
    public void scheduledIngestion() {
        log.info("Scheduled ingestion triggered");
        ingestionService.ingestAll();
    }
}