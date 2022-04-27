package com.refactorizando.zeebe.example;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ZeebeWorkers {

  private static void logJob(final ActivatedJob job) {
    log.info(
        "complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variables: {}]",
        job.getType(),
        job.getKey(),
        job.getElementId(),
        job.getProcessInstanceKey(),
        Instant.ofEpochMilli(job.getDeadline()),
        job.getCustomHeaders(),
        job.getVariables());
  }

  @ZeebeWorker(type = "classify", name = "main-worker")
  public void classifyEmergency(final JobClient client, final ActivatedJob job) {
    logJob(job);
    if (job.getVariablesAsMap().get("emergencyReason")
        == null) { // default to ambulance if no reason is provided
      client.newCompleteCommand(job.getKey()).variables("{\"emergencyType\": \"Injured\"}").send()
          .join();
    } else if (job.getVariablesAsMap().get("emergencyReason").toString().contains("person")) {
      client.newCompleteCommand(job.getKey()).variables("{\"emergencyType\": \"Injured\"}").send()
          .join();
    } else if (job.getVariablesAsMap().get("emergencyReason").toString().contains("fire")) {
      client.newCompleteCommand(job.getKey()).variables("{\"emergencyType\": \"Fire\"}").send()
          .join();
    }
  }

  @ZeebeWorker(type = "hospital", name = "main-worker")
  public void handleHospitalCoordination(final JobClient client, final ActivatedJob job) {
    logJob(job);
    client.newCompleteCommand(job.getKey()).send().join();
  }

  @ZeebeWorker(type = "firefighters", name = "main-worker")
  public void handleFirefighterCoordination(final JobClient client, final ActivatedJob job) {
    logJob(job);
    client.newCompleteCommand(job.getKey()).send().join();
  }

}
