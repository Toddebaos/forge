package com.forge.ingestor.model.elasticsearch;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.Instant;

@Data
@NoArgsConstructor
@Document(indexName = "commits")
public class SearchableCommit {

    @Id
    private String sha;

    @Field(type = FieldType.Text, analyzer = "english")
    private String message;

    @Field(type = FieldType.Keyword)
    private String repoFullName;

    @Field(type = FieldType.Keyword)
    private String author;

    @Field(type = FieldType.Date)
    private Instant committedAt;

    private String htmlUrl;
}
