package com.forge.ingestor.repository.mongodb;

import com.forge.ingestor.model.mongodb.RawWebhookEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WebhookEventRepository extends MongoRepository<RawWebhookEvent, String> {
    List<RawWebhookEvent> findByRepoFullName(String repoFullName);
    List<RawWebhookEvent> findByEventType(String eventType);
}