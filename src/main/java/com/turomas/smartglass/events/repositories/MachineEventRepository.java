package com.turomas.smartglass.events.repositories;

import com.turomas.smartglass.events.domain.MachineEvent;
import com.turomas.smartglass.twins.domain.dto.*;
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
  List<MaterialDTO> getMostUsedMaterials(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);

  @Aggregation(
      pipeline = {
        "{$match: {machine: ?0, type: 'end_process', 'params.process_name': {$ne: 'EVACUATE_GLASS'}, timestamp: {$gte: ?1, $lt: ?2}}}",
        "{$group: {_id: {process_name: '$params.process_name', optimization_name: '$params.optimization_name'}, materialsProcessed: {$sum: 1}}}",
        "{$sort: {materialsProcessed: -1}}",
        "{$project: {name: '$_id.optimization_name', processName: '$_id.process_name', materialsProcessed: 1, _id: 0}}"
      })
  List<OptimizationDTO> getOptimizationHistory(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);

  @Aggregation(
      pipeline = {
        "{$match: {$and: [{machine: ?0}, {type: 'end_process'}, {$or: [{'params.process_name': 'CUT'}, {'params.process_name': 'VINIL'}]}, "
            + "{'params.tool_angle': {$exists: true}}, {'params.tool_total_distance_covered': {$exists: true}}, {timestamp: {$gte: ?1, $lt: ?2}}]}}",
        "{$sort: {timestamp: -1}}",
        "{$limit: 1}",
        "{$project: {cuttingAngle: '$params.tool_angle', distanceCovered: '$params.tool_total_distance_covered', _id: 0}}"
      })
  ToolInfoDTO getToolInfo(String machineName, LocalDateTime startDate, LocalDateTime endDate);

  @Aggregation(
      pipeline = {
        "{$match: {$and: [{machine: ?0}, {type: 'end_process'}, {$or: [{'params.process_name': 'LOWE'}, {'params.process_name': 'TPF'}]}, "
            + "{'params.wheel_size': {$exists: true}}, {'params.tool_total_distance_covered': {$exists: true}}, {timestamp: {$gte: ?1, $lt: ?2}}]}}",
        "{$sort: {timestamp: -1}}",
        "{$limit: 1}",
        "{$project: {diameter: '$params.wheel_size', distanceCovered: '$params.tool_total_distance_covered', _id: 0}}"
      })
  WheelInfoDTO getWheelInfo(String machineName, LocalDateTime startDate, LocalDateTime endDate);

  @Aggregation(
      pipeline = {
        "{$match: {machine: ?0, type: 'ERROR', timestamp: {$gte: ?1, $lt: ?2}}}",
        "{$group: {_id: '$error_name', timesOccurred: {$sum: 1}}}",
        "{$sort: {timesOccurred: -1}}",
        "{$project: {errorName: '$_id', timesOccurred: 1, _id: 0}}"
      })
  List<BreakdownDTO> getBreakdownsOccurred(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);

  @Query(value = "{machine: ?0, timestamp: {$gte: ?1, $lt: ?2}}")
  SortedSet<MachineEvent> searchBy(
      String machineName, LocalDateTime startDate, LocalDateTime endDate);
}
