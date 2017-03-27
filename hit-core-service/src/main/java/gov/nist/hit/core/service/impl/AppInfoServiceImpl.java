package gov.nist.hit.core.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.hit.core.domain.AppInfo;
import gov.nist.hit.core.repo.AppInfoRepository;
import gov.nist.hit.core.service.AppInfoService;

@Service
public class AppInfoServiceImpl implements AppInfoService {

  @Autowired
  protected AppInfoRepository appInfoRepository;

  @Autowired
  @PersistenceContext(unitName = "base-tool")
  protected EntityManager entityManager;


  @Override
  public String getRsbVersion() {
    String sql = "select appInfo.rsbVersion from AppInfo appInfo order by appInfo.date DESC";
    Query query = entityManager.createNativeQuery(sql);
    query.setMaxResults(1);
    List<String> list = query.getResultList();
    if (list == null || list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }


  @Override
  public String getUploadPattern() {
    String sql =
        "select appInfo.uploadContentTypes from AppInfo appInfo order by appInfo.date DESC";
    Query query = entityManager.createNativeQuery(sql);
    query.setMaxResults(1);
    List<String> list = query.getResultList();
    if (list == null || list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

  @Override
  public String getUploadMaxSize() {
    String sql = "select appInfo.uploadMaxSize from AppInfo appInfo order by appInfo.date DESC";
    Query query = entityManager.createNativeQuery(sql);
    query.setMaxResults(1);
    List<String> list = query.getResultList();
    if (list == null || list.isEmpty()) {
      return null;
    }
    return list.get(0);
  }

  public AppInfo get() {
    List<AppInfo> infos = appInfoRepository.findAll();
    if (infos != null && !infos.isEmpty()) {
      AppInfo appInfo = infos.get(0);
      return appInfo;
    }
    return null;
  }



}
