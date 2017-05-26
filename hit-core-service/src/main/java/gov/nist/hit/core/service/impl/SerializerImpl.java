package gov.nist.hit.core.service.impl;

import org.springframework.stereotype.Service;

import gov.nist.hit.core.service.Serializer;

@Service
public class SerializerImpl implements Serializer {

  // Gson gson = new GsonBuilder()
  // .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE).create();
  //
  //
  // @Override
  // public void toJson(OutputStream out, MessageModel model) throws IOException {
  // JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
  // writeModel(writer, model);
  // out.flush();
  // out.close();
  // }
  //
  // public void writeModel(JsonWriter writer, MessageModel model) throws IOException {
  // writer.beginObject();
  // if (model.getDelimeters() != null && !model.getDelimeters().isEmpty()) {
  // writer.name("delimeters");
  // writeMap(writer, model.getDelimeters());
  // } else {
  // writer.name("delimeters").nullValue();
  // }
  //
  // if (model.getElements() != null && !model.getElements().isEmpty()) {
  // writer.name("elements");
  // writeElements(writer, model.getElements());
  // } else {
  // writer.name("elements").nullValue();
  // }
  // writer.endObject();
  // }
  //
  // public void writeMap(JsonWriter writer, Map<String, String> map) throws IOException {
  // writer.beginObject();
  // for (String key : map.keySet()) {
  // writer.name(key).value(map.get(key));
  // }
  // writer.endObject();
  // }
  //
  // public void writeElements(JsonWriter writer, List<MessageElement> elements) throws IOException
  // {
  // writer.beginArray();
  // for (MessageElement element : elements) {
  // writeElement(writer, element);
  // }
  // writer.endArray();
  // }
  //
  // public void writeElement(JsonWriter writer, MessageElement element) throws IOException {
  // writer.beginObject();
  // writer.name("type").value(element.getType());
  // writer.name("label").value(element.getLabel());
  // if (element.getData() != null) {
  // writer.name("data");
  // writeData(writer, element.getData());
  // } else {
  // writer.name("data").nullValue();
  // }
  // if (element.getChildren() != null) {
  // writer.name("children");
  // writeElements(writer, element.getChildren());
  // } else {
  // writer.name("children").nullValue();
  // }
  // writer.endObject();
  // }
  //
  // public void writeData(JsonWriter writer, MessageElementData data) throws IOException {
  // writer.beginObject();
  // writer.name("path").value(data.getPath());
  // writer.name("name").value(data.getName());
  // writer.name("usage").value(data.getUsage());
  // writer.name("minOccurs").value(data.getMinOccurs());
  // writer.name("maxOccurs").value(data.getMaxOccurs());
  // writer.name("lineNumber").value(data.getLineNumber());
  // writer.name("startIndex").value(data.getStartIndex());
  // writer.name("endIndex").value(data.getEndIndex());
  // writer.name("position").value(data.getPosition());
  // writer.name("instanceNumber").value(data.getInstanceNumber());
  // writer.name("description").value(data.getDescription());
  // writer.name("value").value(data.getValue());
  // writer.name("type").value(data.getType());
  // writer.endObject();
  // }
  //
  //
  // @Override
  // public String toJson(MessageValidationResult model) {
  // String json = gson.toJson(model);
  // return json;
  // }
  //
  // @Override
  // public byte[] toByte(MessageModel model) throws JsonProcessingException {
  // // ObjectWriter w = objectMapper.writer();
  // // byte[] json = w.with(SerializationFeature.INDENT_OUTPUT)
  // // .without(SerializationFeature.FAIL_ON_EMPTY_BEANS.WRAP_EXCEPTIONS).writeValueAsBytes(model);
  // // return json;
  // return null;
  // }



}
