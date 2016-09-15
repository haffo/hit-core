package gov.nist.auth.hit.core.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
 * <p/>
 * Created by Maxence Lefort on 9/13/16.
 */
@Entity
public class UserTestStepReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    private String xml;
    private String html;
    private Double version;
    private Account account;
    private Long testStepPersistentId;
    private String comments;

    public UserTestStepReport(String xml, String html, Double version, Account account, Long testStepPersistentId, String comments) {
        this.xml = xml;
        this.html = html;
        this.version = version;
        this.account = account;
        this.testStepPersistentId = testStepPersistentId;
        this.comments = comments;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getTestStepPersistentId() {
        return testStepPersistentId;
    }

    public void setTestStepPersistentId(Long testStepPersistentId) {
        this.testStepPersistentId = testStepPersistentId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}