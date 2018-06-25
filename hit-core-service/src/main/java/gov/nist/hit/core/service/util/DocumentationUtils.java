package gov.nist.hit.core.service.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.CFTestStepGroup;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestStep;

public class DocumentationUtils {

  public static void createMessageFile(TestPlan tp, String parentDirName) throws IOException {
    File folder = new File(
        parentDirName + File.separator + tp.getPosition() + "." + tp.getName() + File.separator);
    if (!folder.exists()) {
      folder.mkdirs();
    }
    if (tp.getTestCaseGroups() != null && !tp.getTestCaseGroups().isEmpty()) {
      List<TestCaseGroup> list = new ArrayList<TestCaseGroup>(tp.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup tcg : list) {
        createMessageFile(tcg, folder.getAbsolutePath());
      }
    }
    if (tp.getTestCases() != null && !tp.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<TestCase>(tp.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
        createMessageFile(tc, folder.getAbsolutePath());
      }
    }
  }



  public static void createTestPackageFile(TestPlan tp, String parentDirName) throws IOException {
    File folder = new File(
        parentDirName + File.separator + tp.getPosition() + "." + tp.getName() + File.separator);
    if (tp.getTestPackage() != null && tp.getTestPackage().getPdfPath() != null) {
      if (!folder.exists()) {
        folder.mkdirs();
      }
      String filename = folder.getAbsolutePath() + File.separator + tp.getPosition() + "."
          + tp.getName() + "-TestPackage.pdf";
      File file = new File(filename);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileUtils.copyURLToFile(new URL(tp.getTestPackage().getPdfPath()), file);
    }
  }


  public static void createMessageFile(CFTestPlan tp, String parentDirName) throws IOException {
    File folder = new File(
        parentDirName + File.separator + tp.getPosition() + "." + tp.getName() + File.separator);
    if (tp.getTestStepGroups() != null && !tp.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<CFTestStepGroup>(tp.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup tcg : list) {
        createMessageFile(tcg, folder.getAbsolutePath());
      }
    }
    if (tp.getTestSteps() != null && !tp.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<CFTestStep>(tp.getTestSteps());
      Collections.sort(list);
      for (CFTestStep tc : list) {
        createMessageFile(tc, folder.getAbsolutePath());
      }
    }
  }



  private static void createMessageFile(CFTestStepGroup tp, String parentDirName)
      throws IOException {
    File folder = new File(
        parentDirName + File.separator + tp.getPosition() + "." + tp.getName() + File.separator);
    if (tp.getTestStepGroups() != null && !tp.getTestStepGroups().isEmpty()) {
      List<CFTestStepGroup> list = new ArrayList<CFTestStepGroup>(tp.getTestStepGroups());
      Collections.sort(list);
      for (CFTestStepGroup tcg : list) {
        createMessageFile(tcg, folder.getAbsolutePath());
      }
    }
    if (tp.getTestSteps() != null && !tp.getTestSteps().isEmpty()) {
      List<CFTestStep> list = new ArrayList<CFTestStep>(tp.getTestSteps());
      Collections.sort(list);
      for (CFTestStep tc : list) {
        createMessageFile(tc, folder.getAbsolutePath());
      }
    }
  }

  private static void createMessageFile(TestCaseGroup tp, String parentDirName) throws IOException {
    File folder = new File(
        parentDirName + File.separator + tp.getPosition() + "." + tp.getName() + File.separator);

    if (tp.getTestCaseGroups() != null && !tp.getTestCaseGroups().isEmpty()) {
      List<TestCaseGroup> list = new ArrayList<TestCaseGroup>(tp.getTestCaseGroups());
      Collections.sort(list);
      for (TestCaseGroup tcg : list) {
        createMessageFile(tcg, folder.getAbsolutePath());
      }
    }
    if (tp.getTestCases() != null && !tp.getTestCases().isEmpty()) {
      List<TestCase> list = new ArrayList<TestCase>(tp.getTestCases());
      Collections.sort(list);
      for (TestCase tc : list) {
        createMessageFile(tc, folder.getAbsolutePath());
      }
    }
  }



  private static void createMessageFile(TestCase tp, String parentDirName) throws IOException {
    File folder = new File(
        parentDirName + File.separator + tp.getPosition() + "." + tp.getName() + File.separator);

    if (tp.getTestSteps() != null && !tp.getTestSteps().isEmpty()) {
      List<TestStep> list = new ArrayList<TestStep>(tp.getTestSteps());
      Collections.sort(list);
      for (TestStep tc : list) {
        createMessageFile(tc, folder.getAbsolutePath());
      }
    }
  }



  private static void createMessageFile(CFTestStep tp, String parentDirName) throws IOException {
    File folder = new File(
        parentDirName + File.separator + tp.getPosition() + "." + tp.getName() + File.separator);
    TestContext context = tp.getTestContext();
    if (context != null && context.getMessage() != null
        && !StringUtils.isEmpty(context.getMessage().getContent())) {
      if (!folder.exists()) {
        folder.mkdirs();
      }
      String filename = folder.getAbsolutePath() + File.separator + tp.getPosition() + "."
          + tp.getName() + "-Message.txt";
      File file = new File(filename);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileUtils.copyInputStreamToFile(IOUtils.toInputStream(context.getMessage().getContent()),
          file);
    }
  }

  private static void createMessageFile(TestStep tp, String parentDirName) throws IOException {
    File folder = new File(
        parentDirName + File.separator + tp.getPosition() + "." + tp.getName() + File.separator);
    TestContext context = tp.getTestContext();
    if (context != null && context.getMessage() != null
        && !StringUtils.isEmpty(context.getMessage().getContent())) {
      if (!folder.exists()) {
        folder.mkdirs();
      }
      String filename = folder.getAbsolutePath() + File.separator + tp.getPosition() + "."
          + tp.getName() + "-Message.txt";
      File file = new File(filename);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileUtils.copyInputStreamToFile(IOUtils.toInputStream(context.getMessage().getContent()),
          file);
    }
  }


}
