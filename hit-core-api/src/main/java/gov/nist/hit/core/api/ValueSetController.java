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

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.hit.core.repo.VocabularyLibraryRepository;
import gov.nist.hit.core.service.Streamer;
import io.swagger.annotations.ApiParam;

/**
 * @author Harold Affo (NIST)
 * 
 */

@RequestMapping("/valueSetLibrary")
@RestController
public class ValueSetController {

	Logger logger = LoggerFactory.getLogger(ValueSetController.class);

	@Autowired
	private VocabularyLibraryRepository vocabularyLibraryRepository;

	@Autowired
	private Streamer streamer;

	@RequestMapping(value = "/{valueSetLibraryId}", produces = "application/json", method = RequestMethod.GET)
	public void getValueSetLibraryById(HttpServletResponse response,
			@ApiParam(value = "the id of the value set library", required = true) @PathVariable final long valueSetLibraryId)
			throws IOException {
		logger.info("Fetching value set library (json) with id=" + valueSetLibraryId);
		String value = vocabularyLibraryRepository.getJson(valueSetLibraryId);
		streamer.stream(response.getOutputStream(), value);

	}

}
