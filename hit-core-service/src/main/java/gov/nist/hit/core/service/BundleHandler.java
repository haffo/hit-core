package gov.nist.hit.core.service;

import java.io.IOException;

import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.GVTSaveInstance;
import gov.nist.hit.core.service.exception.ProfileParserException;

public interface BundleHandler {

  public String unzip(byte[] bytes, String path) throws Exception;

  public GVTSaveInstance createGVTSaveInstance(String dir, String domain, String authorUsername,
      boolean preloaded) throws IOException, ProfileParserException;

  public String getProfileContentFromZipDirectory(String dir) throws IOException;

  public String getValueSetContentFromZipDirectory(String dir) throws IOException;

  public String getConstraintContentFromZipDirectory(String dir) throws IOException;

  GVTSaveInstance createGVTSaveInstance(String dir, CFTestPlan tp)
      throws IOException, ProfileParserException;

}
