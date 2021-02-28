package com.turomas.smartglass.machineEvent.repositories;

import com.turomas.smartglass.machineEvent.domain.CuttingMaterial;
import com.turomas.smartglass.machineEvent.domain.MachineEvent;
import com.turomas.smartglass.machineEvent.domain.Optimization;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedSet;

public interface MachineEventRepository extends MongoRepository<MachineEvent, String> {
  @Query(value = "{}")
  SortedSet<MachineEvent> getMachineEvents();

  @Aggregation(
      pipeline = {
        "{$match: {machine: ?0, type: 'end_process', 'params.process_name': 'CUT', 'params.material': {$exists: true}, timestamp: {$gte: ?1, $lt: ?2}}}",
        "{$group: {_id: '$params.material', usedTimes: {$sum: 1}}}",
        "{$sort: {usedTimes: -1}}",
        "{$project: {name: $_id, usedTimes: 1, _id: 0}}"
      })
  List<CuttingMaterial> getMostUsedMaterials(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);

  @Aggregation(
      pipeline = {
        "{$match: {machine: ?0, type: 'end_process', 'params.process_name': {$ne: 'EVACUATE_GLASS'}, timestamp: {$gte: ?1, $lt: ?2}}}",
        "{$group: {_id: {process_name: '$params.process_name', optimization_name: '$params.optimization_name'}, materialsProcessed: {$sum: 1}}}",
        "{$sort: {materialsProcessed: -1}}",
        "{$project: {name: '$_id.optimization_name', processName: '$_id.process_name', materialsProcessed: 1, _id: 0}}"
      })
  List<Optimization> getOptimizationHistory(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);

  @Query(value = "{machine: ?0, timestamp: {$gte: ?1, $lt: ?2}}")
  SortedSet<MachineEvent> searchBy(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);
}
