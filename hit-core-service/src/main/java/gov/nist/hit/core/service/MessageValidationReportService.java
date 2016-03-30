package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.service.exception.ValidationReportException;

import java.io.InputStream;
import java.util.List;

public interface MessageValidationReportService {

  void delete(TestStepValidationReport resutl);

  TestStepValidationReport save(TestStepValidationReport report);

  void delete(Long id);

  TestStepValidationReport findOne(Long id);

  void delete(List<TestStepValidationReport> resutls);

  void save(List<TestStepValidationReport> resutl);

  TestStepValidationReport findOneByTestStepAndUser(Long testStepId, Long userId);

  List<TestStepValidationReport> findAllByTestStepAndUser(Long testStepId, Long userId);

  List<TestStepValidationReport> findAllByTestCaseAndUser(Long testCaseId, Long userId);

  List<TestStepValidationReport> findAllByUser(Long userId);

  TestStepValidationReport findOneByIdAndUser(Long reportId, Long userId);

  String generateHtml(String xml) throws ValidationReportException;

  String generateXhtml(String xml) throws ValidationReportException;

  InputStream generatePdf(String xml) throws ValidationReportException;


}
