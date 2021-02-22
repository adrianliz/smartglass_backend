package com.turomas.smartglass;

import com.turomas.smartglass.domain.MachineEvent;
import com.turomas.smartglass.repositories.MachineEventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartglassApplication implements CommandLineRunner {

  private final MachineEventRepository machineEventRepository;

  public SmartglassApplication(MachineEventRepository machineEventRepository) {
    this.machineEventRepository = machineEventRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(SmartglassApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    for (MachineEvent machineEvent: machineEventRepository.findAll()) {
      System.out.println(machineEvent);
    }
  }
}
