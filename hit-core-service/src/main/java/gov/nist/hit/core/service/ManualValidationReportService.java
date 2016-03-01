package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.ManualValidationResult;
import gov.nist.hit.core.domain.ValidationReport;
import gov.nist.hit.core.service.exception.ValidationReportException;

import java.io.InputStream;
import java.util.List;

public interface ManualValidationReportService {

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

  String toHTML(String content) throws ValidationReportException;

  String toXHTML(String content) throws ValidationReportException;

  InputStream toPDF(String content) throws ValidationReportException;

  String toXML(ManualValidationResult request) throws ValidationReportException;

}
