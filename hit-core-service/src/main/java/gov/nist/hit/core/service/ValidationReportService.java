package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.ValidationReport;
import gov.nist.hit.core.service.exception.ValidationReportException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public interface ValidationReportService {


  void delete(ValidationReport resutl);

  ValidationReport save(ValidationReport resutl);

  void delete(Long id);

  ValidationReport findOne(Long id);

  void delete(List<ValidationReport> resutls);

  void save(List<ValidationReport> resutl);

  ValidationReport findOneByTestStepAndUser(Long testStepId, Long userId);

  List<ValidationReport> findAllByTestCaseAndUser(Long testCaseId, Long userId);

  List<ValidationReport> findAllByUser(Long userId);

  ValidationReport findOneByIdAndUser(Long reportId, Long userId);

  String toHTML(String xml) throws ValidationReportException;

  String toXHTML(String xml) throws ValidationReportException;

  InputStream toPDF(String xml) throws ValidationReportException;

  InputStream zipReports(String folderName, HashMap<String, InputStream> reports)
      throws ValidationReportException;

}
