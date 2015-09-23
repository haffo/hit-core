package gov.nist.hit.core.service.impl;

/*
 * Meaningful Use Core DocumentHelper.java October 14, 2011
 * 
 * This code was produced by the National Institute of Standards and Technology (NIST). See the
 * 'nist.disclaimer' file given in the distribution for information on the use and redistribution of
 * this software.
 */

import org.apache.log4j.Logger;

/**
 * @author Harold Affo (NIST)
 */
public class DocumentHelper {

  private static final Logger logger = Logger.getLogger(DocumentHelper.class);

  private final String RELEASENOTE_PATTERN = "Documentation/ReleaseNotes";

  // public void init() {
  //
  //
  //
  // createReleaseNotes();
  //
  //
  // }
  //
  // /**
  // * @param links
  // * @return
  // */
  // public Map<String, String> getFilesMapping(String filesMapping) {
  // Map<String, String> staticFilesMapping = new LinkedHashMap<String, String>();
  // if (!"".equals(filesMapping.trim())) {
  // String[] fileNamesMappingArray = Pattern.compile("::").split(filesMapping);
  // if (fileNamesMappingArray != null) {
  // for (String fileNamePair : fileNamesMappingArray) {
  // String[] pattern = Pattern.compile("=").split(fileNamePair);
  // if (pattern != null) {
  // if (pattern.length > 0) {
  // String value = fileNamePair.substring(fileNamePair.indexOf("=") + 1);
  // if (value.startsWith("http://") || value.startsWith("https://")) {
  // String key = null;
  // if (pattern.length >= 2) {
  // key = pattern[0];
  // } else {
  // key = getLinkDescription(ResourceHelper.getInstance().getFileName(value));
  // }
  // staticFilesMapping.put(key, value);
  // } else {
  // staticFilesMapping.put(pattern[0], findLatestFilePath(value));
  // }
  // }
  // }
  // }
  // }
  // }
  // return staticFilesMapping;
  // }
  //
  // public String getLinkDescription(String link) {
  // String tmp = null;
  // // default name formatting
  // // replace - and _ by space
  // link = link.substring(0, link.lastIndexOf("."));
  // StringBuilder buf = new StringBuilder();
  //
  // String[] cList = link.split("_");
  // for (String c : cList) {
  // buf.append(c.substring(0, 1).toUpperCase() + c.substring(1) + " ");
  // }
  // cList = buf.toString().split("-");
  // buf = new StringBuilder();
  // for (String c : cList) {
  // buf.append(c.substring(0, 1).toUpperCase() + c.substring(1) + " ");
  // }
  // tmp = buf.toString();
  // return tmp;
  //
  // }
  //
  // /**
  // * @param filesMapping
  // * @return
  // */
  // public DefaultDocument createDefaultDocument(String filesMapping) {
  // List<Document> documentList = new ArrayList<Document>();
  // documentList = loadFiles(getFilesMapping(filesMapping), documentList);
  // return new DefaultDocument(documentList);
  // }
  //
  // /**
  // * @param json
  // * @return
  // * @throws JsonParseException
  // * @throws JsonMappingException
  // * @throws IOException
  // */
  // public DefaultDocument createDocumentFromJson(String json) throws JsonParseException,
  // JsonMappingException, IOException {
  // List<Document> documentList = createArtifactsFromJson(json);
  // return new DefaultDocument(documentList);
  // }
  //
  //
  //
  // /**
  // *
  // * @param folder
  // * @param fileNames
  // * @return
  // */
  // public List<Document> releaseNotes() {
  // List<Resource> resources = ResourcebundleHelper.getResources(RELEASENOTE_PATTERN);
  // if (resources != null && !resources.isEmpty()) {
  // List<Document> releaseNotes = new ArrayList<Document>();
  // Document document = null;
  // String fileName =
  // String fileName = file.substring(length + 1);
  // String version = fileName.substring(fileName.indexOf(".") + 1, fileName.lastIndexOf("."));
  // document = new Document();
  // document.setVersion(version);
  // document.setDescription(fileName);
  // document.setPath(file);
  // document.setType(DocumentType.RELEASENOTE);
  // releaseNotes.add(document);
  // }
  //
  // Collections.sort(releaseNotes, new Comparator<Document>() {
  // @Override
  // public int compare(Document o1, Document o2) {
  // // TODO Auto-generated method stub
  // return o2.getVersion().compareTo(o1.getVersion());
  // }
  // });
  // return releaseNotes;
  //
  //
  // }
  //
  //
  //
  // throw new IllegalArgumentException(
  // "Failed to create the release notes. No release note document found");
  // }
  //
  //
  // /**
  // *
  // * @param prefix
  // * @return
  // */
  // public void createReleaseNotes() {
  // // build the release notes dynamically
  // if (releaseNotesPrefix != null) {
  // List<Document> releaseNotes = new ArrayList<Document>();
  // Document art = new Document();
  // art.setVersion("1.0.0");
  // art.setDescription("Initial Release");
  // releaseNotes.add(art);
  // if (!"".equals(releaseNotesPrefix.trim())) {
  // if (releaseNotesPrefix.contains("*")) {
  // releaseNotesPrefix = releaseNotesPrefix.substring(0, releaseNotesPrefix.lastIndexOf("*"));
  // }
  // String folder =
  // releaseNotesPrefix.substring(0,
  // releaseNotesPrefix.lastIndexOf(CoreConstants.CLASS_PATH_FILE_SEP));
  // ResourceBox box = resourceBundle.createResourceBox(folder);
  // releaseNotes
  // .addAll((createReleaseNoteArtifacts(folder, box.startWith(releaseNotesPrefix))));
  // }
  // Collections.sort(releaseNotes, new Comparator<Document>() {
  // @Override
  // public int compare(Document o1, Document o2) {
  // // TODO Auto-generated method stub
  // return o2.getVersion().compareTo(o1.getVersion());
  // }
  // });
  //
  // releaseNotesDocument = new DefaultDocument(releaseNotes);
  // } else if (releaseNotesDocument != null) { // release notes provided
  // // sort
  // Collections.sort(this.releaseNotesDocument.getDocumentList(), new Comparator<Document>() {
  // @Override
  // public int compare(Document o1, Document o2) {
  // return o2.getVersion().compareTo(o1.getVersion());
  // }
  // });
  // }
  // }
  //
  // /**
  // * @param staticPattern
  // */
  // public List<Document> loadFiles(Map<String, String> staticMappings, List<Document>
  // documentList) {
  // if (staticMappings != null && documentList != null) {
  // ResourceHelper helper = ResourceHelper.getInstance();
  // for (String key : staticMappings.keySet()) {
  // String value = staticMappings.get(key);
  // // not a link and a file
  // if (!isRecognizedFile(value) && !value.startsWith("http")) {
  // documentList.add(new Document(value, key));
  // } else {
  // String fileName = helper.getFileName(value);
  // boolean link = value.startsWith("http");
  // documentList.add(new Document(fileName, key, value, helper.getVersion(fileName), helper
  // .getExtension(fileName), helper.getCurrentTimeStamp(), link));
  // }
  // }
  // }
  // return documentList;
  // }
  //
  // /**
  // *
  // * @param json
  // * @return
  // * @throws JsonParseException
  // * @throws JsonMappingException
  // * @throws IOException
  // */
  // public List<Document> createArtifactsFromJson(String json) throws JsonParseException,
  // JsonMappingException, IOException {
  // if (json != null && json != null) {
  // ObjectMapper mapper = new ObjectMapper();
  // List<Document> artifacts = mapper.readValue(json, new TypeReference<List<Document>>() {});
  // return artifacts;
  // }
  // return null;
  // }
  //
  // /**
  // *
  // * @param fileName
  // * @return
  // */
  // private boolean isRecognizedFile(String fileName) {
  // if (fileName.contains(".")) {
  // String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
  // extension = extension.toLowerCase();
  // return extension.equals("doc") || extension.equals("txt") || extension.equals("pdf")
  // || extension.equals("html") || extension.equals("er7") || extension.equals("xml")
  // || extension.equals("xslt") || extension.equals("xsl") || extension.equals("docx")
  // || extension.equals("xlsx") || extension.equals("ppt") || extension.equals("pptx");
  // }
  // return false;
  // }
  //
  //
  // /**
  // * return the latest file path
  // *
  // * @param prefix: file path prefix (no variable included)
  // * @return
  // */
  // public String findLatestFilePath(String path) {
  // if (path != null && path.contains("*")) {
  // int length = path.lastIndexOf("*");
  // path = path.substring(0, length);
  // String folder = path.substring(0, path.lastIndexOf(CoreConstants.CLASS_PATH_FILE_SEP));
  // ResourceBox box = resourceBundle.createResourceBox(folder);
  // path = box.getLastestFilePath(path);
  // if (path == null)
  // throw new IllegalArgumentException("Failed to find the latest of file at " + path);
  // }
  // return path;
  // }
  //
  // /**
  // * @param resourceDirectoryPath
  // * @return
  // */
  // public ResourceBox createResourceBox(String resourceDirectoryPath) {
  // return ResourceBundle.getInstance().createResourceBox(resourceDirectoryPath);
  // }
  //
  // public ResourceBundle getResourceBundle() {
  // return resourceBundle;
  // }
  //
  // public void setResourceBundle(ResourceBundle resourceBundle) {
  // this.resourceBundle = resourceBundle;
  // }

}
