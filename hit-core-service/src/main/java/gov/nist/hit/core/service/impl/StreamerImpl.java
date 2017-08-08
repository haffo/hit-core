package gov.nist.hit.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import gov.nist.hit.core.domain.AbstractTestCase;
import gov.nist.hit.core.domain.CFTestPlan;
import gov.nist.hit.core.domain.CFTestStep;
import gov.nist.hit.core.domain.DataMapping;
import gov.nist.hit.core.domain.Document;
import gov.nist.hit.core.domain.Json;
import gov.nist.hit.core.domain.Message;
import gov.nist.hit.core.domain.MessageElement;
import gov.nist.hit.core.domain.MessageElementData;
import gov.nist.hit.core.domain.MessageModel;
import gov.nist.hit.core.domain.MessageValidationResult;
import gov.nist.hit.core.domain.Protocol;
import gov.nist.hit.core.domain.TestArtifact;
import gov.nist.hit.core.domain.TestCase;
import gov.nist.hit.core.domain.TestCaseDocument;
import gov.nist.hit.core.domain.TestCaseDocumentation;
import gov.nist.hit.core.domain.TestCaseGroup;
import gov.nist.hit.core.domain.TestContext;
import gov.nist.hit.core.domain.TestPlan;
import gov.nist.hit.core.domain.TestStep;
import gov.nist.hit.core.domain.TestStepValidationReport;
import gov.nist.hit.core.domain.Transaction;
import gov.nist.hit.core.domain.ValidationResult;
import gov.nist.hit.core.domain.ValidationResultItem;
import gov.nist.hit.core.service.Streamer;
import gov.nist.hit.core.service.util.GCUtil;

@Service
public class StreamerImpl implements Streamer {

  JsonFactory jfactory = new JsonFactory();


  @Override
  public void stream(OutputStream os, MessageModel model) throws IOException {
    JsonGenerator writer = createGenerator(os);
    if (model != null) {
      writer.writeStartObject();
      if (model.getDelimeters() != null && !model.getDelimeters().isEmpty()) {
        writer.writeFieldName("delimeters");
        writeStringMap(writer, model.getDelimeters());
      } else {
        writer.writeNullField("delimeters");
      }

      if (model.getElements() != null && !model.getElements().isEmpty()) {
        writer.writeArrayFieldStart("elements");
        writeElements(writer, model.getElements());
        writer.writeEndArray();
      } else {
        writer.writeStringField("elements", null);
      }
      writer.writeEndObject();
    } else {
      writer.writeNull();
    }
    writer.close();
  }

  @Override
  public void stream(OutputStream os, MessageValidationResult model) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (model != null) {
      jGenerator.writeStartObject();
      writeLongField(jGenerator, "reportId", model.getReportId());
      jGenerator.writeStringField("json", model.getJson());
      jGenerator.writeEndObject();
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }

  @Override
  public void stream(OutputStream os, TestStepValidationReport report) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (report != null) {
      jGenerator.writeStartObject();
      jGenerator.writeNumberField("id", report.getId());
      jGenerator.writeStringField("html", report.getHtml());
      jGenerator.writeStringField("xml", report.getXml());
      jGenerator.writeStringField("json", report.getJson());
      jGenerator.writeEndObject();
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();

  }

  @Override
  public void stream(OutputStream os, ValidationResult result) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (result != null) {
      jGenerator.writeStartObject();
      jGenerator.writeStringField("html", result.getHtml());
      jGenerator.writeStringField("xml", result.getXml());

      jGenerator.writeFieldName("errors");
      if (result.getErrors() != null) {
        write(jGenerator, result.getErrors());
      } else {
        jGenerator.writeNull();
      }

      jGenerator.writeFieldName("alerts");
      if (result.getAlerts() != null) {
        write(jGenerator, result.getAlerts());
      } else {
        jGenerator.writeNull();
      }

      jGenerator.writeFieldName("warnings");
      if (result.getWarnings() != null) {
        write(jGenerator, result.getWarnings());
      } else {
        jGenerator.writeNull();
      }

      jGenerator.writeFieldName("affirmatives");
      if (result.getAffirmatives() != null) {
        write(jGenerator, result.getAffirmatives());

      } else {
        jGenerator.writeNull();
      }
      jGenerator.writeFieldName("ignores");
      if (result.getIgnores() != null) {
        write(jGenerator, result.getIgnores());
      } else {
        jGenerator.writeNull();
      }
      jGenerator.writeEndObject();
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();

  }

  private void write(JsonGenerator jGenerator, List<ValidationResultItem> items)
      throws IOException {
    if (items != null) {
      jGenerator.writeStartArray();
      for (ValidationResultItem item : items) {
        write(jGenerator, item);
      }
      jGenerator.writeEndArray();
    }
  }

  private void write(JsonGenerator jGenerator, ValidationResultItem item) throws IOException {
    if (item != null) {
      jGenerator.writeStartObject();
      jGenerator.writeStringField("description", item.getDescription());
      jGenerator.writeNumberField("column", item.getColumn());
      jGenerator.writeNumberField("line", item.getLine());
      jGenerator.writeStringField("path", item.getPath());
      jGenerator.writeStringField("failureSeverity", item.getFailureSeverity());
      jGenerator.writeStringField("elementContent", item.getElementContent());
      jGenerator.writeStringField("assertionDeclaration", item.getAssertionDeclaration());
      jGenerator.writeStringField("userComment", item.getUserComment());
      jGenerator.writeStringField("assertionResult", item.getAssertionResult());
      jGenerator.writeStringField("failureType", item.getFailureType());
      jGenerator.writeEndObject();
    }
  }



  @Override
  public void stream(OutputStream os, String content) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    jGenerator.writeString(content);
    jGenerator.close();
  }

  @Override
  public void stream(OutputStream os, InputStream io) throws IOException {
    GCUtil.performGC();
    FileCopyUtils.copy(io, os);
  }

  // @Override
  // public void stream(OutputStream os, TestPlan testPlan) throws IOException {
  // JsonGenerator jGenerator = createGenerator(os);
  // if (testPlan != null) {
  // write(jGenerator, testPlan);
  // } else {
  // jGenerator.writeNull();
  // }
  // jGenerator.close();
  // }

  @Override
  public void stream(OutputStream os, TestArtifact artifact) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (artifact != null) {
      write(jGenerator, artifact);
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }


  @Override
  public void stream(OutputStream os, Map<String, Object> artifacts) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (artifacts != null) {
      write(jGenerator, artifacts);
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }



  // @Override
  // public void stream(OutputStream os, TestCase testCase) throws IOException {
  // JsonGenerator jGenerator = createGenerator(os);
  // if (testCase != null) {
  // write(jGenerator, testCase);
  // } else {
  // jGenerator.writeNull();
  // }
  // jGenerator.close();
  // }

  // @Override
  // public void stream(OutputStream os, Json json) throws IOException {
  // JsonGenerator jGenerator = createGenerator(os);
  // if (json != null) {
  // write(jGenerator, json);
  // } else {
  // jGenerator.writeNull();
  // }
  // jGenerator.close();
  // }


  //
  // @Override
  // public void stream(OutputStream os, TestStep testStep) throws IOException {
  // JsonGenerator jGenerator = createGenerator(os);
  // if (testStep != null) {
  // write(jGenerator, testStep, true);
  // } else {
  // jGenerator.writeNull();
  // }
  // jGenerator.close();
  // }

  @Override
  public void streamDocs(OutputStream os, List<Document> documents) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (documents != null) {
      jGenerator.writeStartArray();
      for (Document document : documents) {
        write(jGenerator, document);
      }
      jGenerator.writeEndArray();
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }

  @Override
  public void stream(OutputStream os, Document document) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (document != null) {
      write(jGenerator, document);
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }

  @Override
  public void stream(OutputStream os, TestCaseDocumentation document) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (document != null) {
      write(jGenerator, document);
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();

  }



  private void write(JsonGenerator jGenerator, TestStep testStep, boolean endObject)
      throws IOException {
    if (testStep != null) {
      jGenerator.writeStartObject();
      writeAbstractTest(jGenerator, testStep);
      writeLongField(jGenerator, "id", testStep.getId());
      jGenerator.writeStringField("testingType",
          testStep.getTestingType() != null ? testStep.getTestingType().toString() : null);
      if (testStep.getProtocols() != null) {
        jGenerator.writeArrayFieldStart("protocols");
        for (Protocol protocol : testStep.getProtocols()) {
          write(jGenerator, protocol);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("protocols");
      }
      TestContext testContext = testStep.getTestContext();
      if (testContext != null) {
        jGenerator.writeFieldName("testContext");
        write(jGenerator, testContext);
      } else {
        jGenerator.writeNullField("testContext");
      }
      if (endObject)
        jGenerator.writeEndObject();
    }
  }

  private void write(JsonGenerator jGenerator, TestStep testStep) throws IOException {
    write(jGenerator, testStep, true);
  }


  // @Override
  // public void stream(OutputStream os, CFTestStep testStep) throws IOException {
  // JsonGenerator jGenerator = createGenerator(os);
  // if (testStep != null) {
  // write(jGenerator, testStep);
  // } else {
  // jGenerator.writeNull();
  // }
  // jGenerator.close();
  // }


  @Override
  public void stream2(OutputStream os, List<CFTestPlan> testPlans) throws IOException {
    // TODO Auto-generated method stub
    JsonGenerator jGenerator = createGenerator(os);
    if (testPlans != null) {
      jGenerator.writeStartArray();
      for (CFTestPlan child : testPlans) {
        write(jGenerator, child);
      }
      jGenerator.writeEndArray();
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }


  @Override
  public void stream(OutputStream os, List<TestPlan> testPlans) throws IOException {
    // TODO Auto-generated method stub
    JsonGenerator jGenerator = createGenerator(os);
    if (testPlans != null) {
      jGenerator.writeStartArray();
      for (TestPlan child : testPlans) {
        write(jGenerator, child);
      }
      jGenerator.writeEndArray();
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }


  //
  // @Override
  // public void stream(OutputStream os, CFTestPlan testPlan) throws IOException {
  // // TODO Auto-generated method stub
  // JsonGenerator jGenerator = createGenerator(os);
  // if (testPlan != null) {
  // write(jGenerator, testPlan);
  // } else {
  // jGenerator.writeNull();
  // }
  // jGenerator.close();
  //
  // }



  // @Override
  // public void stream(OutputStream os, TestContext testContext) throws IOException {
  // JsonGenerator jGenerator = createGenerator(os);
  // if (testConte= null) {
  // write(jGenerator, testContext);
  // } else {
  // jGenerator.writeNull();
  // }
  // jGenerator.close();
  // }


  private void write(JsonGenerator jGenerator, Json json) throws IOException {
    if (json != null) {
      jGenerator.writeStartObject();
      jGenerator.writeStringField("value", json.value());
      jGenerator.writeEndObject();
    }
  }


  private void write(JsonGenerator jGenerator, TestCaseDocumentation document) throws IOException {
    if (document != null) {
      jGenerator.writeStartObject();
      writeLongField(jGenerator, "id", document.getId());
      jGenerator.writeStringField("title", document.getTitle());
      jGenerator.writeStringField("stage",
          document.getStage() != null ? document.getStage().toString() : null);
      jGenerator.writeStringField("json", document.getJson());
      if (document.getChildren() != null) {
        jGenerator.writeArrayFieldStart("children");
        for (TestCaseDocument tcDoc : document.getChildren()) {
          write(jGenerator, tcDoc);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("children");
      }
      jGenerator.writeEndObject();
    }
  }

  private void write(JsonGenerator jGenerator, TestCaseDocument document) throws IOException {
    if (document != null) {
      jGenerator.writeStartObject();
      writeLongField(jGenerator, "id", document.getId());
      jGenerator.writeStringField("title", document.getTitle());
      jGenerator.writeStringField("type", document.getType());
      jGenerator.writeStringField("mcPath", document.getMcPath());
      jGenerator.writeStringField("tdsPath", document.getTdsPath());
      jGenerator.writeStringField("tpPath", document.getTpPath());
      jGenerator.writeStringField("tpsPath", document.getTpsPath());
      jGenerator.writeStringField("jdPath", document.getJdPath());
      jGenerator.writeStringField("tsPath", document.getTsPath());
      jGenerator.writeStringField("csPath", document.getCsPath());
      jGenerator.writeStringField("format", document.getFormat());
      jGenerator.writeEndObject();
    }
  }



  private void write(JsonGenerator jGenerator, TestCaseGroup testCaseGroup) throws IOException {
    if (testCaseGroup != null) {
      jGenerator.writeStartObject();
      writeAbstractTest(jGenerator, testCaseGroup);
      writeLongField(jGenerator, "id", testCaseGroup.getId());
      if (testCaseGroup.getTestCases() != null) {
        jGenerator.writeArrayFieldStart("testCases");
        for (TestCase child : testCaseGroup.getTestCases()) {
          write(jGenerator, child);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("testCases");
      }

      if (testCaseGroup.getTestCaseGroups() != null) {
        jGenerator.writeArrayFieldStart("testCaseGroups");
        for (TestCaseGroup child : testCaseGroup.getTestCaseGroups()) {
          write(jGenerator, child);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("testCaseGroups");
      }
      jGenerator.writeEndObject();
    }
  }


  private void write(JsonGenerator jGenerator, CFTestPlan testPlan) throws IOException {
    if (testPlan != null) {
      jGenerator.writeStartObject();
      writeAbstractTest(jGenerator, testPlan);
      writeLongField(jGenerator, "id", testPlan.getId());
      if (testPlan.getTestCases() != null) {
        jGenerator.writeArrayFieldStart("children");
        for (CFTestStep child : testPlan.getTestCases()) {
          write(jGenerator, child);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("children");
      }
      jGenerator.writeEndObject();
    }
  }


  private void write(JsonGenerator jGenerator, TestPlan testPlan) throws IOException {
    if (testPlan != null) {
      jGenerator.writeStartObject();
      writeAbstractTest(jGenerator, testPlan);
      jGenerator.writeBooleanField("transport", testPlan.isTransport());
      jGenerator.writeStringField("domain", testPlan.getDomain());
      writeLongField(jGenerator, "id", testPlan.getId());

      if (testPlan.getTestPackage() != null) {
        jGenerator.writeObjectFieldStart("testPackage");
        jGenerator.writeStringField("pdfPath", testPlan.getTestPackage().getPdfPath());
        writeLongField(jGenerator, "id", testPlan.getTestPackage().getId());
        jGenerator.writeStringField("name", testPlan.getTestPackage().getName());
        jGenerator.writeEndObject();
      } else {
        jGenerator.writeNullField("testPackage");
      }

      if (testPlan.getTestPlanSummary() != null) {
        jGenerator.writeObjectFieldStart("testPlanSummary");
        jGenerator.writeStringField("pdfPath", testPlan.getTestPackage().getPdfPath());
        writeLongField(jGenerator, "id", testPlan.getTestPackage().getId());
        jGenerator.writeStringField("name", testPlan.getTestPackage().getName());
        jGenerator.writeEndObject();
      } else {
        jGenerator.writeNullField("testPlanSummary");
      }

      if (testPlan.getTestCases() != null) {
        jGenerator.writeArrayFieldStart("testCases");
        for (TestCase child : testPlan.getTestCases()) {
          write(jGenerator, child);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("testCases");
      }

      if (testPlan.getTestCaseGroups() != null) {
        jGenerator.writeArrayFieldStart("testCaseGroups");
        for (TestCaseGroup child : testPlan.getTestCaseGroups()) {
          write(jGenerator, child);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("testCaseGroups");
      }
      jGenerator.writeEndObject();
    }
  }

  private void write(JsonGenerator jGenerator, TestArtifact artifact) throws IOException {
    if (artifact != null) {
      jGenerator.writeStartObject();
      jGenerator.writeStringField("pdfPath", artifact.getPdfPath());
      jGenerator.writeStringField("json", artifact.getJson());
      jGenerator.writeStringField("html", artifact.getHtml());
      writeLongField(jGenerator, "id", artifact.getId());
      jGenerator.writeStringField("name", artifact.getName());
      jGenerator.writeEndObject();
    }
  }



  private void writeAbstractTest(JsonGenerator jGenerator, AbstractTestCase testObject)
      throws IOException {
    if (testObject != null) {
      jGenerator.writeStringField("name", testObject.getName());
      writeLongField(jGenerator, "persistentId", testObject.getPersistentId());
      jGenerator.writeStringField("description", testObject.getDescription());
      jGenerator.writeStringField("type",
          testObject.getType() != null ? testObject.getType().toString() : null);
      jGenerator.writeStringField("stage",
          testObject.getStage() != null ? testObject.getStage().toString() : null);
      jGenerator.writeNumberField("position", testObject.getPosition());
      jGenerator.writeStringField("version", testObject.getVersion() + "");
      jGenerator.writeStringField("scope",
          testObject.getScope() != null ? testObject.getScope().toString() : null);
      jGenerator.writeBooleanField("preloaded", testObject.isPreloaded());
      if (testObject.getSupplements() != null) {
        jGenerator.writeArrayFieldStart("supplements");
        for (Document doc : testObject.getSupplements()) {
          write(jGenerator, doc);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("supplements");
      }
    }
  }

  private void write(JsonGenerator jGenerator, TestCase testCase) throws IOException {
    if (testCase != null) {
      jGenerator.writeStartObject();
      writeAbstractTest(jGenerator, testCase);
      writeLongField(jGenerator, "id", testCase.getId());
      if (testCase.getTestSteps() != null) {
        jGenerator.writeArrayFieldStart("testSteps");
        for (TestStep testStep : testCase.getTestSteps()) {
          write(jGenerator, testStep);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("testSteps");
      }

      if (testCase.getDataMappings() != null) {
        jGenerator.writeArrayFieldStart("dataMappings");
        for (DataMapping mapping : testCase.getDataMappings()) {
          write(jGenerator, mapping);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("dataMappings");
      }
      jGenerator.writeEndObject();
    }
  }



  private void write(JsonGenerator jGenerator, CFTestStep testStep) throws IOException {
    if (testStep != null) {
      write(jGenerator, testStep, false);
      if (testStep.getChildren() != null) {
        jGenerator.writeArrayFieldStart("children");
        for (CFTestStep child : testStep.getChildren()) {
          write(jGenerator, child);
        }
        jGenerator.writeEndArray();
      } else {
        jGenerator.writeNullField("children");
      }
      jGenerator.writeEndObject();
    }
  }


  private void write(JsonGenerator jGenerator, TestContext testContext) throws IOException {
    if (testContext != null) {
      jGenerator.writeStartObject();
      writeLongField(jGenerator, "id", testContext.getId());
      jGenerator.writeStringField("format", testContext.getFormat());
      jGenerator.writeStringField("type",
          testContext.getType() != null ? testContext.getType().toString() : null);
      if (testContext.getMessage() != null) {
        jGenerator.writeObjectFieldStart("message");
        Message message = testContext.getMessage();
        writeLongField(jGenerator, "id", message.getId());
        jGenerator.writeStringField("name", message.getName());
        jGenerator.writeStringField("description", message.getDescription());
        jGenerator.writeStringField("content", message.getContent());
        jGenerator.writeEndObject();
      } else {
        jGenerator.writeNullField("message");
      }
      jGenerator.writeEndObject();
    }
  }


  private void write(JsonGenerator jGenerator, Protocol protocol) throws IOException {
    if (protocol != null) {
      jGenerator.writeStartObject();
      jGenerator.writeStringField("value", protocol.getValue());
      jGenerator.writeNumberField("position", protocol.getPosition());
      jGenerator.writeBooleanField("default", protocol.isDefaut());
      jGenerator.writeEndObject();

    }
  }


  private void write(JsonGenerator jGenerator, Document document) throws IOException {
    if (document != null) {
      jGenerator.writeStartObject();
      writeLongField(jGenerator, "id", document.getId());
      jGenerator.writeStringField("title", document.getTitle());
      jGenerator.writeStringField("name", document.getName());
      jGenerator.writeStringField("path", document.getPath());
      jGenerator.writeStringField("version", document.getVersion());
      jGenerator.writeStringField("comments", document.getComments());
      jGenerator.writeStringField("date", document.getDate());
      jGenerator.writeNumberField("position", document.getPosition());
      jGenerator.writeEndObject();
    }
  }



  private JsonGenerator createGenerator(OutputStream os) throws IOException {
    return createGenerator(os, true);
  }

  private JsonGenerator createGenerator(OutputStream os, boolean performGC) throws IOException {
    if (performGC)
      GCUtil.performGC();
    return jfactory.createGenerator(os, JsonEncoding.UTF8);
  }



  private void write(JsonGenerator jGenerator, Map<String, Object> map) throws IOException {
    if (map != null) {
      jGenerator.writeStartObject();
      for (String key : map.keySet()) {
        jGenerator.writeFieldName(key);
        writeValue(jGenerator, map.get(key));
      }
      jGenerator.writeEndObject();
    }
  }

  private void writeValue(JsonGenerator jGenerator, Object value) throws IOException {
    if (value != null) {
      if (value instanceof String) {
        jGenerator.writeString((value.toString()));
      } else if (value instanceof TestArtifact) {
        write(jGenerator, (TestArtifact) value);
      } else if (value instanceof Document) {
        write(jGenerator, (Document) value);
      } else if (value instanceof Collection) {
        jGenerator.writeStartArray();
        Collection coll = (Collection) value;
        for (Object obj : coll) {
          writeValue(jGenerator, obj);
        }
        jGenerator.writeEndArray();
      } else {
        throw new IllegalArgumentException("Unsupported streaming object ");
      }
    } else {
      jGenerator.writeNull();
    }
  }


  private void writeStringMap(JsonGenerator jGenerator, Map<String, String> map)
      throws IOException {
    if (map != null) {
      jGenerator.writeStartObject();
      for (String key : map.keySet()) {
        Object obj = map.get(key);
        jGenerator.writeFieldName(key);
        jGenerator.writeString((obj.toString()));
      }
      jGenerator.writeEndObject();
    }
  }



  private void writeElements(JsonGenerator writer, List<MessageElement> elements)
      throws IOException {
    for (MessageElement element : elements) {
      writeElement(writer, element);
    }
  }


  private void write(JsonGenerator jGenerator, DataMapping mapping) throws IOException {
    if (mapping != null) {
      jGenerator.writeStartObject();
      writeLongField(jGenerator, "id", mapping.getId());
      jGenerator.writeBooleanField("optional", mapping.getOptional());
      if (mapping.getTarget() != null) {
        jGenerator.writeObjectFieldStart("target");
        writeLongField(jGenerator, "id", mapping.getTarget().getId());
        jGenerator.writeStringField("field", mapping.getTarget().getField());
        jGenerator.writeEndObject();
      } else {
        jGenerator.writeNullField("target");
      }

      if (mapping.getSource() != null) {
        jGenerator.writeObjectFieldStart("source");
        writeLongField(jGenerator, "id", mapping.getSource().getId());
        jGenerator.writeEndObject();
      } else {
        jGenerator.writeNullField("source");
      }
      jGenerator.writeEndObject();
    }

  }


  public void writeElement(JsonGenerator writer, MessageElement element) throws IOException {
    if (element != null) {
      writer.writeStartObject();
      writer.writeStringField("type", element.getType());
      writer.writeStringField("label", element.getLabel());
      if (element.getData() != null) {
        writer.writeFieldName("data");
        writeData(writer, element.getData());
      } else {
        writer.writeStringField("data", null);
      }
      if (element.getChildren() != null) {
        writer.writeArrayFieldStart("children");
        writeElements(writer, element.getChildren());
        writer.writeEndArray();
      } else {
        writer.writeStringField("children", null);
      }
      writer.writeEndObject();
    }
  }

  public void writeData(JsonGenerator writer, MessageElementData data) throws IOException {
    if (data != null) {
      writer.writeStartObject();
      writer.writeStringField("path", data.getPath());
      writer.writeStringField("name", data.getName());
      writer.writeStringField("usage", data.getUsage());
      writer.writeNumberField("minOccurs", data.getMinOccurs());
      writer.writeStringField("maxOccurs", data.getMaxOccurs());
      // writer.writeNumberField("lineNumber", data.getLineNumber());

      writer.writeObjectFieldStart("start");
      writer.writeNumberField("line", data.getStart().getLine());
      writer.writeNumberField("index", data.getStart().getIndex());
      writer.writeEndObject();

      writer.writeObjectFieldStart("end");
      writer.writeNumberField("line", data.getEnd().getLine());
      writer.writeNumberField("index", data.getEnd().getIndex());
      writer.writeEndObject();
      //
      //
      //
      // writer.writeNumberField("startIndex", data.getStartIndex());
      // writer.writeNumberField("endIndex", data.getEndIndex());
      writer.writeNumberField("position", data.getPosition());
      writer.writeNumberField("column", data.getPosition());
      writer.writeNumberField("instanceNumber", data.getInstanceNumber());
      writer.writeStringField("description", data.getDescription());
      writer.writeStringField("value", data.getValue());
      writer.writeStringField("type", data.getType());
      writer.writeEndObject();
    }
  }

  @Override
  public void stream(OutputStream os, TestCaseGroup testCaseGroup) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (testCaseGroup != null) {
      write(jGenerator, testCaseGroup);
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();

  }

  @Override
  public void stream(OutputStream os, Transaction transaction) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (transaction != null) {
      write(jGenerator, transaction);
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }

  private void write(JsonGenerator jGenerator, Transaction transaction) throws IOException {
    if (transaction != null) {
      jGenerator.writeStartObject();
      writeLongField(jGenerator, "id", transaction.getId());
      jGenerator.writeStringField("incoming", transaction.getIncoming());
      jGenerator.writeStringField("outgoing", transaction.getOutgoing());
      writeLongField(jGenerator, "testStepId", transaction.getTestStepId());
      jGenerator.writeEndObject();
    }
  }

  private void writeLongField(JsonGenerator jGenerator, String key, Long value) throws IOException {
    if (value == null) {
      jGenerator.writeNullField(key);
    } else {
      jGenerator.writeNumberField(key, value);
    }

  }

  @Override
  public void streamMap(OutputStream os, Map<String, String> map) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (map != null) {
      jGenerator.writeStartObject();
      for (String key : map.keySet()) {
        jGenerator.writeFieldName(key);
        writeValue(jGenerator, map.get(key));
      }
      jGenerator.writeEndObject();
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }

  @Override
  public void streamElements(OutputStream os, List<MessageElement> elements) throws IOException {
    JsonGenerator jGenerator = createGenerator(os);
    if (elements != null) {
      jGenerator.writeStartArray();
      writeElements(jGenerator, elements);
      jGenerator.writeEndArray();
    } else {
      jGenerator.writeNull();
    }
    jGenerator.close();
  }



}
