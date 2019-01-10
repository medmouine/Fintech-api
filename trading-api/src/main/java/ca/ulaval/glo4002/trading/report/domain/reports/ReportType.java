package ca.ulaval.glo4002.trading.report.domain.reports;

public enum ReportType {
  DAILY;

  public static Boolean contains(String reportType) {
    try {
      ReportType.valueOf(reportType);
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }
}
