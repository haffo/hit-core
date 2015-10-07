package gov.nist.hit.core.service;


public interface ZipGenerator {

  /**
   * Generate the zip file of files matching the specific pattern
   * 
   * @param pattern
   * @param name: The name of the zip file
   * @return the path to the zip file
   * @throws Exception
   */
  public String generate(String pattern, String name) throws Exception;


}
