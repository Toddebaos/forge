package com.forge.ingestor.model.mongodb;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "webhook_events")
public class RawWebhookEvent {

    @Id
    private String id;

    private String eventType;       // t.ex. "push", "pull_request", "issues"
    private String repoFullName;    // vilket repo eventet kom från
    private Instant receivedAt;     // när Forge tog emot det

    // Hela payload:en lagras som en Map — schema-on-read
    // Vi vet inte exakt vilka fält GitHub skickar för varje eventtyp
    private Map<String, Object> payload;
}