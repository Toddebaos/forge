package com.forge.ingestor.repository.elasticsearch;

import com.forge.ingestor.model.elasticsearch.SearchableCommit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SearchableCommitRepository extends ElasticsearchRepository<SearchableCommit, String> {
    List<SearchableCommit> findByRepoFullName(String repoFullName);
    List<SearchableCommit> findByAuthor(String author);
}
