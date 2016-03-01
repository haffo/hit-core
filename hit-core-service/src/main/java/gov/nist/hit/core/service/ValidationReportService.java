package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.ManualValidationResult;
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

  List<ValidationReport> findAllByTestStepAndUser(Long testStepId, Long userId);

  List<ValidationReport> findAllByTestCaseAndUser(Long testCaseId, Long userId);

  List<ValidationReport> findAllByUser(Long userId);

  ValidationReport findOneByIdAndUser(Long reportId, Long userId);

  InputStream zipReports(String folderName, HashMap<String, InputStream> reports)
      throws ValidationReportException;

  String toAutoHTML(String content) throws ValidationReportException;

  String toAutoXHTML(String content) throws ValidationReportException;

  InputStream toAutoPDF(String content) throws ValidationReportException;

  String toManualHTML(String content) throws ValidationReportException;

  String toManualXHTML(String content) throws ValidationReportException;

  InputStream toManualPDF(String content) throws ValidationReportException;

  String toManualXML(ManualValidationResult validationResult);

}
