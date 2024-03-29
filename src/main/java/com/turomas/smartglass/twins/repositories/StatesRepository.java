package com.turomas.smartglass.twins.repositories;

import com.turomas.smartglass.twins.domain.statesmachine.TwinState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.SortedSet;

public interface StatesRepository extends MongoRepository<TwinState, String> {
  @Query(value = "{twinName: ?0}")
  Page<TwinState> getStates(String twinName, Pageable pageable);

  @Query(value = "{twinName: ?0, 'lastEventEvaluated.timestamp': {$gt: ?1}, 'enterEvent.timestamp': {$lt: ?2}}")
  SortedSet<TwinState> getOverlapStates(String twinName, LocalDateTime startDate, LocalDateTime endDate);
}
