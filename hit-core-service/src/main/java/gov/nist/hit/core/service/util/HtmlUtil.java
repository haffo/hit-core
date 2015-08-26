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

public class HtmlUtil {

  public static String repairStyle(String htmlReport) {
    htmlReport = htmlReport.replaceAll("&#xA;row&#xA;&#xA;1&#xA;", "row1");
    htmlReport = htmlReport.replaceAll("&#xA;row&#xA;&#xA;2&#xA;", "row2");
    htmlReport = htmlReport.replaceAll("&#xA;row&#xA;&#xA;3&#xA;", "row3");
    htmlReport = htmlReport.replaceAll("&#xA;row&#xA;&#xA;4&#xA;", "row4");
    htmlReport = htmlReport.replaceAll("&#xA;row&#xA;&#xA;5&#xA;", "row5");
    return htmlReport;
  }

  public static String htmlToXhtml(String html) {
    html = html.replaceAll("type=\"text\">", "type=\"text\" value=\"\"></input>");
    html = html.replaceAll("type=\"checkbox\">", "type=\"checkbox\" value=\"\"></input>");
    html = html.replaceAll("<br>", "<br></br>");
    html = html.replaceAll("<br/>", "<br></br>");
    return html;
  }
}
