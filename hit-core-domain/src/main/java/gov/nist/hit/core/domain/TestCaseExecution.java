package gov.nist.hit.core.domain;

import javax.persistence.*;
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
 *
 * Created by Maxence Lefort on 1/27/16.
 */
@Entity
public class TestCaseExecution implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne
    protected TestCase testCase;

    @Column
    protected Long userId;

    @Column
    protected Long currentTestStepId;

    public TestCaseExecution() {
        super();
    }

    public TestCaseExecution(Long id, TestCase testCase, Long userId) {
        super();
        this.id = id;
        this.testCase = testCase;
        this.userId = userId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public Long getCurrentTestStepId() {
        return currentTestStepId;
    }

    public void setCurrentTestStepId(Long currentTestStepId) {
        this.currentTestStepId = currentTestStepId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
