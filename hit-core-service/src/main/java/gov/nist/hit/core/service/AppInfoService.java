package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.AppInfo;

public interface AppInfoService {

  public String getRsbVersion();

  public String getUploadPattern();

  public String getUploadMaxSize();

  public AppInfo get();

}
