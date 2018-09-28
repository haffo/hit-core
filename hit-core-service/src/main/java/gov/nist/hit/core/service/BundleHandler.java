package gov.nist.hit.core.service;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.CFTestStepGroup;
import gov.nist.hit.core.domain.GVTSaveInstance;
import gov.nist.hit.core.service.exception.ProfileParserException;

public interface BundleHandler {

  public String unzip(byte[] bytes, String path) throws Exception;

  public GVTSaveInstance createSaveInstance(String dir, String domain, String authorUsername,
      boolean preloaded) throws IOException, ProfileParserException;

  public String getProfileContentFromZipDirectory(String dir) throws IOException;

  public String getValueSetContentFromZipDirectory(String dir) throws IOException;

  public String getConstraintContentFromZipDirectory(String dir) throws IOException;

  GVTSaveInstance createSaveInstance(String dir, CFTestPlan tp)
      throws IOException, ProfileParserException;

  GVTSaveInstance createSaveInstance(String dir, CFTestStepGroup tp)
      throws IOException, ProfileParserException;

  public Set<File> findFiles(String dir,String fileName);
}
