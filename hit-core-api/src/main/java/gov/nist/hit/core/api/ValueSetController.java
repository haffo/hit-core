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

import gov.nist.hit.core.domain.Json;
import gov.nist.hit.core.repo.VocabularyLibraryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harold Affo (NIST)
 * 
 */

@RequestMapping("/valueSetLibrary")
@RestController
@Api(value = "ValueSetLibrary", tags = "ValueSet Library")
public class ValueSetController {

  Logger logger = LoggerFactory.getLogger(ValueSetController.class);

  @Autowired
  private VocabularyLibraryRepository vocabularyLibraryRepository;

  @ApiOperation(value = "Get the value set library by its id", nickname = "getValueSetLibraryById")
  @RequestMapping(value = "/{valueSetLibraryId}", produces = "application/json",
      method = RequestMethod.GET)
  public Json getValueSetLibraryById(@ApiParam(value = "the id of the value set library",
      required = true) @PathVariable final long valueSetLibraryId) {
    logger.info("Fetching value set library (json) with id=" + valueSetLibraryId);
    String value = vocabularyLibraryRepository.getJson(valueSetLibraryId);
    return new Json(value);
  }

}
