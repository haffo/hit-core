package gov.nist.hit.core.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

public class DocumentZipper {

  Map<String, File> cachedFolders = new HashMap<String, File>();

  public InputStream zip(List<Resource> resources) throws IOException {
    File root = null;
    for (Resource resource : resources) {
      File folder = createFiles(resource);
      if (root == null) {
        root = folder;
      } else {
        if (folder.getPath().indexOf(root.getPath()) < 0) {

        }
      }
    }
    return null;
  }

  private File createFiles(Resource resource) throws IOException {
    String filePath = resource.getFilename();
    filePath = filePath.substring(filePath.lastIndexOf(".jar!") + 1);
    String folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
    File folder = null;
    if (!cachedFolders.containsKey(folderPath)) {
      folder = new File(folderPath);
      folder.mkdirs();
      cachedFolders.put(folderPath, folder);
    }
    folder = cachedFolders.get(folderPath);
    PrintWriter pw = new PrintWriter(new File(filePath));
    pw.println(resource.getInputStream());
    pw.close();
    return folder;
  }



}
