package gov.nist.hit.core.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.nist.hit.core.service.ZipGenerator;
import gov.nist.hit.core.service.util.FileUtil;
import gov.nist.hit.core.service.util.ResourcebundleHelper;

public class ZipGeneratorImpl implements ZipGenerator {

  public ZipGeneratorImpl() {

  }

  @Override
  // @Cacheable(value = "HitCache", key = "#type")
  public String generate(String pattern, String type) throws Exception {
    List<Resource> resources = ResourcebundleHelper.getResources(pattern);
    if (resources != null) {
      Path path = Files.createTempDirectory(null);
      File rootFolder = path.toFile();
      if (!rootFolder.exists()) {
        rootFolder.mkdir();
      }
      String folderToZip = rootFolder.getAbsolutePath() + File.separator + "ToZip";
      for (Resource resource : resources) {
        createFile(resource, folderToZip);
      }
      String zipFilename = rootFolder + File.separator + type + ".zip";
      zip(zipFilename, folderToZip);
      return zipFilename;
    }
    return null;

  }


  @Override
  public void zip(String zipFileName, String dir) throws Exception {
    File dirObj = new File(dir);
    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
    addFile(dirObj, out, dir);
    out.close();
  }

  static void addFile(File dirObj, ZipOutputStream out, String folderToZipPath) throws IOException {
    File[] files = dirObj.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        addFile(files[i], out, folderToZipPath);
        continue;
      }
      String absolutePath = files[i].getAbsolutePath();
      FileInputStream in = new FileInputStream(absolutePath);
      String localPath = absolutePath
          .substring(absolutePath.indexOf(folderToZipPath) + folderToZipPath.length() + 1);
      ZipEntry zipEntry = new ZipEntry(localPath);
      out.putNextEntry(zipEntry);
      out.write(IOUtils.toByteArray(in));
      out.closeEntry();
    }
  }


  void createFile(Resource resource, String folderToZip) throws IOException {
    String filePath = resource.getURL().getPath();
    String folderPath = filePath.substring(0, filePath.lastIndexOf(File.separator));
    String resourceRootFolder = folderPath.substring(folderPath.indexOf(".jar!/") + 6);
    File folder = new File(folderToZip + File.separator + resourceRootFolder + File.separator);
    if (!folder.exists()) {
      folder.mkdirs();
    }
    String filename = folder.getAbsolutePath() + File.separator + resource.getFilename();
    File file = new File(filename);
    if (!file.exists()) {
      file.createNewFile();
    }
    FileUtils.copyURLToFile(resource.getURL(), file);

  }


  private boolean isSkipped(File file) throws JsonProcessingException, IOException {
    File target = null;
    if (file.isFile()) {
      target = new File(file.getParent());
    } else {
      target = file;
    }
    return isDirSkipped(target.getName());
  }

  private boolean isDirSkipped(String dirname) throws JsonProcessingException, IOException {
    Resource resource = ResourcebundleHelper.getResource(dirname + "/TestPlan.json");
    if (resource != null) {
      return isTestPlanSkipped(resource);
    } else {
      File file = new File(dirname);
      String parent = file.getParent();
      File dir = new File(parent);
      resource = ResourcebundleHelper.getResource(file.getParent() + "/TestPlan.json");
      if (resource != null) {
        return isTestPlanSkipped(resource);
      } else {
        boolean skipped = isDirSkipped(dir.getName());
        if (skipped) {
          return true;
        } else {
          return isDirSkipped(dir.getParent());
        }
      }
    }
  }

  private boolean isTestPlanSkipped(Resource resource) throws JsonProcessingException, IOException {
    String descriptorContent = FileUtil.getContent(resource);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode testPlanJson = mapper.readTree(descriptorContent);
    boolean skipped = testPlanJson.findValue("skipped") != null
        ? testPlanJson.findValue("skipped").asBoolean() : false;
    return skipped;
  }



  public static void main(String[] args) throws Exception {
    ZipGeneratorImpl gen = new ZipGeneratorImpl();
    try {
      gen.generate("/*Contextbased/**/TestPackage.pdf", "CBTestPackage");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


}
