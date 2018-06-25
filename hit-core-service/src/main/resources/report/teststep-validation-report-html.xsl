<xsl:stylesheet exclude-result-prefixes="map" version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:map="urn:internal"
	xmlns:message="http://www.nist.gov/healthcare/validation/message"
	xmlns:teststepvalidationreport="http://www.nist.gov/healthcare/validation/teststep/report"
	xmlns:manualvalidationreport="http://www.nist.gov/healthcare/validation/manual/report"
	xmlns:report="http://www.nist.gov/healthcare/validation/message/hl7/v2/report">

	<xsl:import href="message-validation-report-html.xsl" />
	<xsl:import href="manual-validation-report-html.xsl" />

	<xsl:output method="html" />

	<xsl:param name="withHeader">
		<xsl:value-of select="true()" />
	</xsl:param>
	<xsl:param name="withTestCaseReference">
		<xsl:value-of select="true()" />
	</xsl:param>

	<xsl:param name="withManualBody">
		<xsl:value-of select="false()" />
	</xsl:param>


	<xsl:template match="teststepvalidationreport:TestStepValidationReport">
		<xsl:apply-templates
			select="teststepvalidationreport:TestStepValidationReportHeader" />
		<xsl:apply-templates
			select="teststepvalidationreport:TestStepValidationReportBody" />
	</xsl:template>

	<xsl:template match="teststepvalidationreport:TestStepValidationReportHeader">

  		<xsl:variable name="stepType"
			select="teststepvalidationreport:Type"></xsl:variable>

		<!-- <xsl:if test="$withHeader = boolean('true')"> -->
		<div class="report-section">
			<table class="forumline title-background" width="100%"
				cellspacing="1" cellpadding="10">
				<tbody class="cf-tbody">
					<tr>
						<td class="row1 border_right">
							<span class="submaintitle">
								<xsl:value-of select="teststepvalidationreport:Position" />
								- Test Step Validation Report
							</span>
						</td>
						<td class="row2" style="font-weight:bold">
							<center>
								<xsl:value-of select="teststepvalidationreport:DateOfTest" />
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
						<td class="row1 border_right">Name</td>
						<td class="row2">
							<center>
								<xsl:value-of select="teststepvalidationreport:Name" />
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
								<xsl:value-of select="teststepvalidationreport:ValidationType" />
							</center>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- </xsl:if> -->
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody class="cf-tbody">
					<tr>
						<td class="row1 border_right">Test Step Type</td>
						<td class="row2">
							<center>
								<xsl:value-of select="teststepvalidationreport:Type" />
							</center>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<xsl:if
			test="$stepType != 'SUT_MANUAL' and $stepType != 'TA_MANUAL'">
			<div class="report-section">
				<table class="forumline" width="100%" cellspacing="1"
					cellpadding="2">
					<tbody class="cf-tbody">
						<tr>
							<td class="row1 border_right">
								Message Validation Result</td>
							<td class="row2">
								<center>
									<xsl:variable name="errorCount"
										select="../teststepvalidationreport:TestStepValidationReportBody/report:HL7V2MessageValidationReport/report:HeaderReport/message:ErrorCount"></xsl:variable>
									<xsl:if test="$errorCount = 0">
										Passed
									</xsl:if>
									<xsl:if test="$errorCount > 0">
										Failed
									</xsl:if>
								</center>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</xsl:if>

		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody class="cf-tbody">
					<tr>
						<td class="row1 border_right">Test Step Outcome</td>
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
							<div style="width:100%;height:100%">
								<textarea style="width:100%;height:100%" readonly="true"
									wrap="off">
									<xsl:value-of select="teststepvalidationreport:Comments" />
								</textarea>

							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<xsl:apply-templates select="teststepvalidationreport:TestCaseReference" />

	</xsl:template>

	<xsl:template match="teststepvalidationreport:TestCaseReference">
		<xsl:if test="$withHeader = boolean('true')">
			<div class="report-section">

				<table class="forumline" width="100%" cellspacing="1"
					cellpadding="2">
					<tbody>
						<tr class="border_bottom">
							<td class="row1 border_right" valign="top" rowspan="4">Test Case
							</td>
							<td class="row2 border_right dark-gray">Test Plan</td>
							<td class="row3">
								<xsl:value-of select="teststepvalidationreport:TestPlan" />
							</td>
						</tr>
						<tr class="border_bottom">
							<td class="row2 border_right dark-gray">Test Group</td>
							<td class="row3">
								<xsl:value-of select="teststepvalidationreport:TestGroup" />
							</td>
						</tr>
						<tr class="border_bottom">
							<td class="row2 border_right dark-gray">Test Case</td>
							<td class="row3">
								<xsl:value-of select="teststepvalidationreport:TestCase" />
							</td>
						</tr>
						<tr class="border_bottom">
							<td class="row2 border_right dark-gray">Test Step</td>
							<td class="row3">
								<xsl:value-of select="teststepvalidationreport:TestStep" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="teststepvalidationreport:TestStepValidationReportBody">

		<xsl:apply-templates select="report:HL7V2MessageValidationReport">
			<!-- <xsl:with-param name="withHeader"> <xsl:value-of select="boolean('false')" 
				/> </xsl:with-param> -->
		</xsl:apply-templates>

		<xsl:apply-templates select="manualvalidationreport:ManualValidationReport">
			<!-- <xsl:with-param name="withHeader"> <xsl:value-of select="boolean('false')" 
				/> </xsl:with-param> -->
		</xsl:apply-templates>
	</xsl:template>

</xsl:stylesheet>
