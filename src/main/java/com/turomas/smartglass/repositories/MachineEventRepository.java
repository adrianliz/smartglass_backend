package com.turomas.smartglass.repositories;

import com.turomas.smartglass.domain.MachineEvent;
import org.springframework.data.repository.CrudRepository;

public interface MachineEventRepository extends CrudRepository<MachineEvent, String> {
}
