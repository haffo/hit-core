<xsl:stylesheet exclude-result-prefixes="map" version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:map="urn:internal"
	xmlns:testcasevalidationreport="http://www.nist.gov/healthcare/validation/testcase/report"
	xmlns:teststepvalidationreport="http://www.nist.gov/healthcare/validation/teststep/report"
	xmlns:manualvalidationreport="http://www.nist.gov/healthcare/validation/manual/report"
	xmlns:report="http://www.nist.gov/healthcare/validation/message/hl7/v2/report">

	<xsl:import href="teststep-validation-report-pdf.xsl" />
 
	<xsl:output method="html" />
 
	<xsl:param name="withHeader">
		<xsl:value-of select="false()" />
	</xsl:param>
	<xsl:param name="withTestCaseReference">
		<xsl:value-of select="false()" />
	</xsl:param>
	
	<xsl:param name="withManualBody">
		<xsl:value-of select="false()" />
	</xsl:param>
	


	<xsl:template match="testcasevalidationreport:TestCaseValidationReport">
		<xsl:apply-templates
			select="testcasevalidationreport:TestCaseValidationReportHeader" />
		<xsl:apply-templates
			select="testcasevalidationreport:TestCaseValidationReportBody" />
	</xsl:template>

	<xsl:template match="testcasevalidationreport:TestCaseValidationReportHeader">
		<div class="report-section">
			<table class="forumline title-background" width="100%"
				cellspacing="1" cellpadding="10">
				<tbody class="cf-tbody">
					<tr>
						<td class="row1 border_right">
							<span class="maintitle">TestCase Validation Report</span>
						</td>
						<td class="row2" style="font-weight:bold">
							<center>
								<xsl:value-of select="testcasevalidationreport:DateOfTest" />
							</center>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody class="cf-tbody">
					<tr>
						<td class="row1 border_right">Validation Type</td>
						<td class="row2">
							<center>
								<xsl:value-of select="testcasevalidationreport:ValidationType" />
							</center>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody class="cf-tbody">
					<tr>
						<td class="row1 border_right">Result</td>
						<td class="row2">
							<center>
								<xsl:value-of select="@Result" />
							</center>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody>
					<tr class="row1">
						<th style="border-bottom:2pt #005C99 solid" align="left">Comments</th>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray ">
							<div style="width:95%;">
								<xsl:for-each select="tokenize(testcasevalidationreport:Comments,'\r')">
									<p>
										<xsl:call-template name="commentsBreaker">
											<xsl:with-param name="commentLine">
												<xsl:value-of select="." />
											</xsl:with-param>
										</xsl:call-template>
									</p>
								</xsl:for-each>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<!-- <div class="report-section"> <table class="forumline" width="100%" 
			cellspacing="1" cellpadding="2"> <tbody class="cf-tbody"> <tr> <td class="row1 
			border_right">Testing Type</td> <td class="row2"> <center> <xsl:value-of 
			select="testcasevalidationreport:Type" /> </center> </td> </tr> </tbody> 
			</table> </div> -->
		<xsl:apply-templates select="testcasevalidationreport:TestCaseReference" />

	</xsl:template>

	<xsl:template match="testcasevalidationreport:TestCaseReference">
		<div class="report-section">
			
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody>
					<tr class="border_bottom">
						<td class="row1 border_right" valign="top" rowspan="4">Test Case</td>
						<td class="row2 border_right dark-gray">Test Plan</td>
						<td class="row3">
							<xsl:value-of select="testcasevalidationreport:TestPlan" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Test Group</td>
						<td class="row3">
							<xsl:value-of select="testcasevalidationreport:TestGroup" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Test Case</td>
						<td class="row3">
							<xsl:value-of select="testcasevalidationreport:TestCase" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</xsl:template>

	<xsl:template match="testcasevalidationreport:TestCaseValidationReportBody">
		
		<xsl:apply-templates
			select="teststepvalidationreport:TestStepValidationReport" />
	</xsl:template>
 
<!-- <xsl:template
		match="testcasevalidationreport:TestStepValidationReports">
		<xsl:apply-templates
			select="teststepvalidationreport:TestStepValidationReport">
		</xsl:apply-templates>
	</xsl:template> -->

</xsl:stylesheet>
