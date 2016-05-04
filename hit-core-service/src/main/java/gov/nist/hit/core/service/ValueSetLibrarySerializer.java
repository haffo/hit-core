package gov.nist.hit.core.service;

import gov.nist.hit.core.domain.ContentDefinitionType;
import gov.nist.hit.core.domain.ExtensibilityType;
import gov.nist.hit.core.domain.NoValidation;
import gov.nist.hit.core.domain.StabilityType;
import gov.nist.hit.core.domain.StatusType;
import gov.nist.hit.core.domain.UsageType;
import gov.nist.hit.core.domain.ValueSetDefinition;
import gov.nist.hit.core.domain.ValueSetDefinitions;
import gov.nist.hit.core.domain.ValueSetElement;
import gov.nist.hit.core.domain.ValueSetLibrary;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import nu.xom.Attribute;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class ValueSetLibrarySerializer {

  public String toXml(ValueSetLibrary valueSetLibrary) {
    nu.xom.Element elmTableLibrary = new nu.xom.Element("ValueSetLibrary");
    elmTableLibrary.setNamespaceURI("http://www.nist.gov/healthcare/data");
    elmTableLibrary.addAttribute(new Attribute("ValueSetIdentifier", valueSetLibrary
        .getValueSetIdentifier()));
    elmTableLibrary.addAttribute(new Attribute("Status", valueSetLibrary.getStatus().toString()));
    elmTableLibrary.addAttribute(new Attribute("ValueSetVersion", valueSetLibrary
        .getValueSetVersion()));
    elmTableLibrary.addAttribute(new Attribute("OrganizationName", valueSetLibrary
        .getOrganizationName()));
    elmTableLibrary.addAttribute(new Attribute("Name", valueSetLibrary.getName()));
    elmTableLibrary.addAttribute(new Attribute("Description", valueSetLibrary.getDescription()));

    if (valueSetLibrary.getNoValidation() != null) {
      nu.xom.Element noValidation = new nu.xom.Element("NoValidation");
      for (String id : valueSetLibrary.getNoValidation().getIds()) {
        nu.xom.Element idElement = new nu.xom.Element("id");
        idElement.appendChild(id);
        noValidation.appendChild(idElement);
      }
    }

    for (ValueSetDefinitions vDefs : valueSetLibrary.getValueSetDefinitions()) {
      nu.xom.Element elmValueSetDefinitions = new nu.xom.Element("ValueSetDefinitions");
      elmValueSetDefinitions.addAttribute(new Attribute("Group", (vDefs.getGrouping() == null) ? ""
          : vDefs.getGrouping()));
      elmValueSetDefinitions.addAttribute(new Attribute("Order", vDefs.getPosition() + ""));
      elmTableLibrary.appendChild(elmValueSetDefinitions);

      for (ValueSetDefinition t : vDefs.getValueSetDefinitions()) {
        nu.xom.Element elmTableDefinition = new nu.xom.Element("ValueSetDefinition");
        elmValueSetDefinitions.appendChild(elmTableDefinition);
        elmTableDefinition.addAttribute(new Attribute("BindingIdentifier", (t
            .getBindingIdentifier() == null) ? "" : t.getBindingIdentifier()));
        elmTableDefinition.addAttribute(new Attribute("Name", (t.getName() == null) ? "" : t
            .getName()));
        elmTableDefinition.addAttribute(new Attribute("NoCodeDisplayText", (t
            .getNoCodeDisplayText() == null) ? "" : t.getNoCodeDisplayText()));
        elmTableDefinition.addAttribute(new Attribute("Description",
            (t.getDescription() == null) ? "" : t.getDescription()));
        elmTableDefinition.addAttribute(new Attribute("Version", (t.getVersion() == null) ? "" : ""
            + t.getVersion()));
        elmTableDefinition
            .addAttribute(new Attribute("Oid", (t.getOid() == null) ? "" : t.getOid()));
        elmTableDefinition.addAttribute(new Attribute("Extensibility",
            (t.getExtensibility() == null) ? "" : t.getExtensibility().value()));
        elmTableDefinition.addAttribute(new Attribute("Stability", (t.getStability() == null) ? ""
            : t.getStability().value()));
        elmTableDefinition.addAttribute(new Attribute("ContentDefinition", (t
            .getContentDefinition() == null) ? "" : t.getContentDefinition().value()));
        if (t.getValueSetElements() != null) {
          for (ValueSetElement c : t.getValueSetElements()) {
            nu.xom.Element elmTableElement = new nu.xom.Element("ValueElement");
            elmTableElement.addAttribute(new Attribute("Value", c.getValue() == null ? "" : c
                .getValue()));
            elmTableElement.addAttribute(new Attribute("DisplayName",
                (c.getDisplayName() == null) ? "" : c.getDisplayName()));
            elmTableElement.addAttribute(new Attribute("CodeSystem",
                (c.getCodeSystem() == null) ? "" : c.getCodeSystem()));
            elmTableElement.addAttribute(new Attribute("CodeSystemVersion", (c
                .getCodeSystemVersion() == null) ? "" : c.getCodeSystemVersion()));

            elmTableElement.addAttribute(new Attribute("Comments", (c.getComments() == null) ? ""
                : c.getComments()));
            elmTableElement.addAttribute(new Attribute("Usage", (c.getUsage() == null) ? "" : c
                .getUsage().value()));
            elmTableDefinition.appendChild(elmTableElement);
          }
        }

      }
    }

    nu.xom.Document doc = new nu.xom.Document(elmTableLibrary);

    return doc.toXML();
  }

  public ValueSetLibrary toObject(String xml) {
    Document tableLibraryDoc = this.toDoc(xml);
    ValueSetLibrary valueSetLibrary = new ValueSetLibrary();
    Element elmTableLibrary =
        (Element) tableLibraryDoc.getElementsByTagName("ValueSetLibrary").item(0);
    valueSetLibrary.setDescription(elmTableLibrary.getAttribute("Description"));
    valueSetLibrary.setName(elmTableLibrary.getAttribute("Name"));
    valueSetLibrary.setOrganizationName(elmTableLibrary.getAttribute("OrganizationName"));
    valueSetLibrary.setValueSetIdentifier(elmTableLibrary.getAttribute("ValueSetIdentifier"));
    valueSetLibrary.setValueSetVersion(elmTableLibrary.getAttribute("ValueSetVersion"));
    if (StringUtils.isNotEmpty(elmTableLibrary.getAttribute("Status"))) {
      valueSetLibrary.setStatus(StatusType.valueOf(elmTableLibrary.getAttribute("Status")));
    }
    NodeList noValidationElements = elmTableLibrary.getElementsByTagName("NoValidation");
    if (noValidationElements != null && noValidationElements.getLength() > 0) {
      Element noValidationElement = (Element) noValidationElements.item(0);
      NoValidation noVal = new NoValidation();
      NodeList idElements = noValidationElement.getElementsByTagName("BindingIdentifier");
      for (int i = 0; i < idElements.getLength(); i++) {
        Element idEl = (Element) idElements.item(i);
        String id = idEl.getTextContent();
        noVal.getIds().add(id);
      }
      valueSetLibrary.setNoValidation(noVal);
    }
    NodeList valueSetDefinitionsElements =
        elmTableLibrary.getElementsByTagName("ValueSetDefinitions");

    if (valueSetDefinitionsElements != null && valueSetDefinitionsElements.getLength() > 0) {
      for (int k = 0; k < valueSetDefinitionsElements.getLength(); k++) {
        Element valueSetDefinitionsElement = (Element) valueSetDefinitionsElements.item(k);
        ValueSetDefinitions valueSetDefinitions = new ValueSetDefinitions();
        valueSetDefinitions
            .setGrouping(valueSetDefinitionsElement.getAttribute("Group") != null ? valueSetDefinitionsElement
                .getAttribute("Group") : "");
        valueSetDefinitions.setPosition(valueSetDefinitionsElement.getAttribute("Order") != null
            && !"".equals(valueSetDefinitionsElement.getAttribute("Order")) ? Integer
            .parseInt(valueSetDefinitionsElement.getAttribute("Order")) : 1);
        valueSetLibrary.getValueSetDefinitions().add(valueSetDefinitions);
        NodeList nodes = valueSetDefinitionsElement.getElementsByTagName("ValueSetDefinition");
        for (int i = 0; i < nodes.getLength(); i++) {
          Element elmTable = (Element) nodes.item(i);
          ValueSetDefinition tableObj = new ValueSetDefinition();
          tableObj.setBindingIdentifier(elmTable.getAttribute("BindingIdentifier"));
          tableObj.setName(elmTable.getAttribute("Name"));

          if (StringUtils.isNotEmpty(elmTable.getAttribute("NoCodeDisplayText"))) {
            tableObj.setNoCodeDisplayText(elmTable.getAttribute("NoCodeDisplayText"));
          }

          if (StringUtils.isNotEmpty(elmTable.getAttribute("Description"))) {
            tableObj.setDescription(elmTable.getAttribute("Description"));
          }

          if (StringUtils.isNotEmpty(elmTable.getAttribute("Oid"))) {
            tableObj.setOid(elmTable.getAttribute("Oid"));
          }

          if (StringUtils.isNotEmpty(elmTable.getAttribute("Version"))) {
            tableObj.setVersion(elmTable.getAttribute("Version"));
          }
          if (StringUtils.isNotEmpty(elmTable.getAttribute("Extensibility"))) {
            tableObj.setExtensibility(ExtensibilityType.fromValue(elmTable
                .getAttribute("Extensibility")));
          }

          if (StringUtils.isNotEmpty(elmTable.getAttribute("Stability"))) {
            tableObj.setStability(StabilityType.fromValue(elmTable.getAttribute("Stability")));
          }

          if (StringUtils.isNotEmpty(elmTable.getAttribute("ContentDefinition"))) {
            tableObj.setContentDefinition(ContentDefinitionType.fromValue(elmTable
                .getAttribute("ContentDefinition")));
          }

          NodeList tableElements = elmTable.getElementsByTagName("ValueElement");

          for (int j = 0; j < tableElements.getLength(); j++) {
            Element elmCode = (Element) tableElements.item(j);
            ValueSetElement codeObj = new ValueSetElement();
            codeObj.setValue(elmCode.getAttribute("Value"));
            codeObj.setCodeSystem(elmCode.getAttribute("CodeSystem"));
            codeObj.setDisplayName(elmCode.getAttribute("DisplayName"));

            if (StringUtils.isNotEmpty(elmCode.getAttribute("CodeSystemVersion"))) {
              codeObj.setCodeSystemVersion(elmCode.getAttribute("CodeSystemVersion"));
            }

            if (StringUtils.isNotEmpty(elmCode.getAttribute("Usage"))) {
              codeObj.setUsage(UsageType.fromValue(elmCode.getAttribute("Usage")));
            }
            codeObj.setComments(elmCode.getAttribute("Comments"));

            tableObj.addValueSetElement(codeObj);
          }

          valueSetDefinitions.addValueSet(tableObj);


        }

      }



    }

    return valueSetLibrary;
  }

  private Document toDoc(String xmlSource) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setIgnoringComments(false);
    factory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder builder;
    try {
      builder = factory.newDocumentBuilder();
      return builder.parse(new InputSource(new StringReader(xmlSource)));
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
