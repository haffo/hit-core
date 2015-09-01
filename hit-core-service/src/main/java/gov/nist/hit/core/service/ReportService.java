package gov.nist.hit.core.service;

import java.util.HashMap;

public interface ReportService {

  /**
   * 
   * @param jsonReport: json report from the validator
   * @return a map of the report formats: expected key are json and html
   * @throws Exception
   */
  public HashMap<String, String> getReports(String jsonReport) throws Exception;
}
