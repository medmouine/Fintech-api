package ca.ulaval.glo4002.trading.report.controller.subcontrollers;

import ca.ulaval.glo4002.trading.report.domain.reports.ReportSubcontroller;
import ca.ulaval.glo4002.trading.report.domain.reports.ReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.List;

@Component
public class ReportSubcontrollerSelector {

  private final List<ReportSubcontroller> reportSubcontrollerList;

  @Autowired
  public ReportSubcontrollerSelector(final DailyReportSubcontroller dailyReportSubcontroller) {
    this.reportSubcontrollerList = Arrays.asList(dailyReportSubcontroller);
  }

  public ReportSubcontroller getSubcontroller(final ReportType reportType) {
    return this.reportSubcontrollerList.stream()
            .filter(subcontroller -> subcontroller.getReportType().equals(reportType))
            .findAny()
            .orElseThrow(NotImplementedException::new);
  }
}
