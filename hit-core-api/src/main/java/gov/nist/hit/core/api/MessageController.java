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

import gov.nist.hit.core.service.exception.MessageDownloadException;
import gov.nist.hit.core.service.exception.MessageException;
import gov.nist.hit.core.service.exception.MessageUploadException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Harold Affo (NIST)
 * 
 */
@RequestMapping("/message")
@RestController
@Api(value = "Messages", hidden = true)
public class MessageController {
  static final Logger logger = LoggerFactory.getLogger(MessageController.class);


  /**
   * TODO:remove
   * 
   * @param er7Message
   * @param request
   * @param response
   * @return
   * @throws MessageException
   */
  @RequestMapping(value = "/download", method = RequestMethod.POST,
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public String download(
      @ApiParam(value = "the content of the message", required = true) @RequestParam("content") String content,
      @ApiParam(value = "the title of the message", required = false) @RequestParam(
          value = "title", required = false) String title, HttpServletRequest request,
      HttpServletResponse response) throws MessageDownloadException {
    try {
      logger.info("Downloading message");
      InputStream io = IOUtils.toInputStream(content, "UTF-8");
      response.setContentType("text/plain");
      String fileName = "Message-" + new Date().getTime() + ".txt";
      response.setHeader("Content-disposition", "attachment;filename=" + fileName);
      FileCopyUtils.copy(io, response.getOutputStream());
    } catch (RuntimeException e) {
      logger.debug("Failed to download the message ");
      throw new MessageDownloadException(e);
    } catch (Exception e) {
      logger.debug("Failed to download the message ");
      throw new MessageDownloadException(e);
    }
    return null;
  }

  /**
   * 
   * @param part
   * @return
   * @throws MessageException
   * @throws MalformedMessageException
   */
  @RequestMapping(value = "/upload", method = RequestMethod.POST,
      consumes = {"multipart/form-data"})
  public Map<String, String> upload(@RequestPart("file") MultipartFile part)
      throws MessageUploadException {
    try {
      Map<String, String> map = new HashMap<String, String>();
      InputStream in = part.getInputStream();
      map.put("name", part.getName());
      map.put("size", part.getSize() + "");
      String content = IOUtils.toString(in);
      map.put("content", content);
      return map;
    } catch (RuntimeException e) {
      throw new MessageUploadException(e);
    } catch (Exception e) {
      throw new MessageUploadException(e);
    }

  }


}
