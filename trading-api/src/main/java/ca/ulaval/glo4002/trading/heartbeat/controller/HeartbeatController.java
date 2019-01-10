package ca.ulaval.glo4002.trading.heartbeat.controller;

import ca.ulaval.glo4002.trading.heartbeat.controller.response.HeartbeatResponse;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/heartbeat")
@RepositoryRestController
public class HeartbeatController {

  @GetMapping("")
  public @ResponseBody
  HeartbeatResponse heartbeat(@RequestParam(value = "token", required = false) final String token) {
    return new HeartbeatResponse(token);
  }

}
