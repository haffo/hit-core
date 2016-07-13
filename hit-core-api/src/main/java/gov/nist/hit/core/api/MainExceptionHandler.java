/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */

package gov.nist.hit.core.api;

import gov.nist.hit.core.domain.ResponseMessage;
import gov.nist.hit.core.service.exception.DocumentationException;
import gov.nist.hit.core.service.exception.DownloadDocumentException;
import gov.nist.hit.core.service.exception.MessageDownloadException;
import gov.nist.hit.core.service.exception.MessageParserException;
import gov.nist.hit.core.service.exception.MessageUploadException;
import gov.nist.hit.core.service.exception.MessageValidationException;
import gov.nist.hit.core.service.exception.ProfileParserException;
import gov.nist.hit.core.service.exception.TestCaseException;
import gov.nist.hit.core.service.exception.TransportException;
import gov.nist.hit.core.service.exception.UserNotFoundException;
import gov.nist.hit.core.service.exception.ValidationReportException;
import gov.nist.hit.core.service.exception.XmlFormatterException;
import gov.nist.hit.core.service.exception.XmlParserException;
import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Harold Affo (NIST)
 * 
 */
@Api(hidden = true)
@ControllerAdvice
public class MainExceptionHandler {
  static final Logger logger = LoggerFactory.getLogger(MainExceptionHandler.class);

  public MainExceptionHandler() {
    super();
  }



  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public String exception(RuntimeException ex) {
    logger.error(ex.getMessage(), ex);
    ex.printStackTrace();
    return "Sorry, something went wrong.\n" + "DEBUG:\n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String exception(Exception ex) {
    logger.error(ex.getMessage(), ex);
    ex.printStackTrace();
    return "Sorry, something went wrong.\n" + "DEBUG:\n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(TestCaseException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String testCaseException(TestCaseException ex) {
    logger.error(ex.getMessage(), ex);
    return "Sorry, something went wrong.\n" + "DEBUG:\n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(MessageValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String messageValidationException(MessageValidationException ex) {
    logger.error(ex.getMessage(), ex);
    return "Sorry, cannot validate the message provided.\n" + "DEBUG:\n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(TransportException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String transportException(TransportException ex) {
    logger.error(ex.getMessage(), ex);
    return ex.getMessage();
  }


  @ResponseBody
  @ExceptionHandler(MessageParserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String messageParserException(MessageParserException ex) {
    logger.error(ex.getMessage(), ex);
    return "Sorry, cannot parse the message provided. \n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(ValidationReportException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String reportException(ValidationReportException ex) {
    logger.error(ex.getMessage(), ex);
    return ex.getMessage() + ". \n";
  }

  @ResponseBody
  @ExceptionHandler(ProfileParserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String profileParserExeption(ProfileParserException ex) {
    logger.error(ex.getMessage(), ex);
    return "Sorry, integrationProfile cannot be parsed.\n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(XmlParserException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String xmlParserException(XmlParserException ex) {
    logger.error(ex.getMessage(), ex);
    return "Malformed xml content:\n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(XmlFormatterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String xmlFormatterException(XmlFormatterException ex) {
    logger.error(ex.getMessage(), ex);
    return "Malformed xml content.\n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(DownloadDocumentException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String documentationException(DocumentationException ex) {
    logger.error(ex.getMessage(), ex);
    return ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(MessageUploadException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String messageUpload(MessageUploadException ex) {
    logger.error(ex.getMessage(), ex);
    return "Sorry, cannot upload the message.\n" + "DEBUG:\n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(MessageDownloadException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String messageDownload(MessageDownloadException ex) {
    logger.error(ex.getMessage(), ex);
    return "Sorry, cannot download the message.\n" + "DEBUG:\n" + ex.getMessage();
  }

  @ResponseBody
  @ExceptionHandler(ConversionFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String conversionFailedException(ConversionFailedException ex) {
    logger.error(ex.getMessage(), ex);
    return "Invalid input.\n";
  }

  @ResponseBody
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String illegalArgumentException(IllegalArgumentException ex) {
    logger.error(ex.getMessage(), ex);
    return "Invalid input.\n";
  }

  @ResponseBody
  @ExceptionHandler(TypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String illegalArgumentException(TypeMismatchException ex) {
    logger.error(ex.getMessage(), ex);
    return "Invalid input.\n";
  }

  @ResponseBody
  @ExceptionHandler(SpelEvaluationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String spelEvaluationException(SpelEvaluationException ex) {
    logger.error(ex.getMessage(), ex);
    return "Invalid input.\n";
  }

  @ResponseBody
  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String userNotFoundException(UserNotFoundException ex) {
    logger.error(ex.getMessage(), ex);
    return "User could not be found.\n";
  }

  @ResponseBody
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseMessage accessDeniedException(AccessDeniedException ex) {
    logger.error("ERROR: Access Denied", ex);
    return new ResponseMessage(ResponseMessage.Type.danger, "accessDenied");
  }



}
