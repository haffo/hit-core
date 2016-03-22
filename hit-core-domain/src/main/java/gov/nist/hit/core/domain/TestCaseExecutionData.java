package gov.nist.hit.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
 * Created by Maxence Lefort on 1/28/16.
 */
@Entity
public class TestCaseExecutionData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @OneToOne
    protected TestStepFieldPair testStepFieldPair;

    @OneToOne
    protected TestCaseExecution testCaseExecution;

    @Column
    protected String data;

    public TestCaseExecutionData() {
        super();
    }

    public TestCaseExecutionData(TestStepFieldPair testStepFieldPair, String data, TestCaseExecution testCaseExecution) {
        this.testStepFieldPair = testStepFieldPair;
        this.data = data;
        this.testCaseExecution = testCaseExecution;
    }

    public TestStepFieldPair getTestStepFieldPair() {
        return testStepFieldPair;
    }

    public void setTestStepFieldPair(TestStepFieldPair testStepFieldPair) {
        this.testStepFieldPair = testStepFieldPair;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TestCaseExecution getTestCaseExecution() {
        return testCaseExecution;
    }

    public void setTestCaseExecution(TestCaseExecution testCaseExecution) {
        this.testCaseExecution = testCaseExecution;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
