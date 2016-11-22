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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.common.io.Files;

/**
 * @author Harold Affo (NIST)
 * 
 */
public class ResourcebundleHelper {

  static PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

  public ResourcebundleHelper() {}

  public static Resource getDescriptorFile(String pattern) throws IOException {
    List<Resource> resources = getResources(pattern);
    return resources != null && resources.size() > 0
        ? resources.get(0).exists() ? resources.get(0) : null : null;
  }

  // public ResourcePatternResolver resourceResolver() {
  // PathMatchingResourcePatternResolver resolver = new
  // PathMatchingResourcePatternResolver();
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

  public static String file(String pattern) {
    if (pattern.endsWith("/")) {
      pattern = pattern.substring(0, pattern.length() - 1);
    }
    return "file:" + pattern;
  }

  public static List<Resource> getResourcesFile(String path) {
    try {
      System.out.println("PATH : " + file(path));
      Resource[] files = resolver.getResources(file(path));
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

  public static Resource getResourceFile(String location) {
    try {
      Resource resource = resolver.getResource(file(location));
      return resource.exists() ? resource : null;
    } catch (RuntimeException e) {
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public static List<Resource> getDirectoriesFile(String pattern) throws IOException {
    List<Resource> resources = getResourcesFile(pattern);
    List<Resource> directories = new ArrayList<Resource>();
    for (Resource res : resources) {
      if (res.getURL().toString().endsWith("/")) {
        directories.add(res);
      }
    }
    return directories;
  }


  public static String getResourcesFromZip(String URL) throws Exception {
    System.out.println("URL : " + URL);
    URL url = new URL(URL);

    // Read URL
    InputStream in = url.openStream();
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[16384];

    while ((nRead = in.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }
    buffer.flush();

    byte[] b = buffer.toByteArray();
    buffer.close();
    System.out.println("READ FINISH");
    // Create TEMP directory
    File tmpDir = Files.createTempDir();
    tmpDir.mkdir();
    if (tmpDir.isDirectory()) {

      // Extract ZIP
      ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(b));
      ZipEntry ze;
      while ((ze = zip.getNextEntry()) != null) {
        String filePath = tmpDir.getAbsolutePath() + File.separator + ze.getName();
        if (!ze.isDirectory()) {
          BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
          byte[] bytesIn = new byte[1024];
          int read = 0;
          while ((read = zip.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
          }
          bos.close();
        } else {
          File dir = new File(filePath);
          dir.mkdir();
        }
        zip.closeEntry();
      }
      zip.close();
      return tmpDir.getAbsolutePath();

    } else {
      throw new Exception("Could not create TMP directory");
    }
  }

}
