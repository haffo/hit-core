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

package gov.nist.hit.core.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author Harold Affo (NIST)
 * 
 */
public class ResourcebundleHelper {


  static PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();


  public ResourcebundleHelper() {}


  public static Resource getDescriptorFile(String pattern) throws IOException {
    List<Resource> resources = getResources(pattern);
    return resources != null && resources.size() > 0 ? resources.get(0).exists() ? resources.get(0)
        : null : null;
  }

  // public ResourcePatternResolver resourceResolver() {
  // PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
  // return resolver;
  // }

  private static String classpath(String path) {
    return "classpath*:" + path;
  }

  public static List<Resource> getResources(String path) {
    try {
      Resource[] files = resolver.getResources(classpath(path));
      List<Resource> resources = Arrays.asList(files);
      Collections.sort(resources, new Comparator<Resource>() {

        @Override
        public int compare(Resource o1, Resource o2) {
          // TODO Auto-generated method stub
          try {
            return o1.getURL().toString().compareTo(o2.getURL().toString());
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          return 0;
        }
      });
      return resources;
    } catch (IOException e1) {
      return new ArrayList<Resource>(1);
    }

  }



  public static Resource getResource(String location) {
    try {
      Resource resource = resolver.getResource(location);
      return resource.exists() ? resource : null;
    } catch (RuntimeException e) {
      return null;
    } catch (Exception e) {
      return null;
    }
  }



  public static List<Resource> getDirectories(String pattern) throws IOException {
    if (!pattern.endsWith("/")) {
      pattern = pattern + "/";
    }
    List<Resource> resources = getResources(pattern);
    List<Resource> directories = new ArrayList<Resource>();
    for (Resource res : resources) {
      if (res.getURL().toString().endsWith("/")) {
        directories.add(res);
      }
    }
    return directories;
  }


}
