<xsl:stylesheet exclude-result-prefixes="map" version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:map="urn:internal">
	<xsl:output method="html" />
	<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />
	<xsl:template match="/ManualValidationReport">
		<xsl:apply-templates select="HeaderReport" />
		<xsl:apply-templates select="TestCaseReference" />
		<xsl:apply-templates select="SpecificReport" />
	</xsl:template>
	<xsl:template match="HeaderReport">
		<div class="report-section">
			<table class="forumline title-background" width="100%"
				cellspacing="1" cellpadding="10">
				<tbody class="cf-tbody">
					<tr>
						<td class="row1 border_right">
							<span class="maintitle">Manual Validation Report</span>
						</td>
						<td class="row2" style="font-weight:bold">
							<center>
								<xsl:value-of select="/ManualValidationReport/HeaderReport/DateOfTest" />
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
								<xsl:value-of
									select="/ManualValidationReport/HeaderReport/ValidationType" />
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
						<td class="row1 border_right">Testing Type</td>
						<td class="row2">
							<center>
								<xsl:value-of select="/ManualValidationReport/HeaderReport/Type" />
							</center>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<!-- <xsl:apply-templates select="message:TestCaseReference" /> -->
	</xsl:template>

	<xsl:template match="TestCaseReference">
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody>
					<tr class="border_bottom">
						<td class="row1 border_right" valign="top" rowspan="6">Test Case</td>
						<td class="row2 border_right dark-gray">TestPlan</td>
						<td class="row3 ">
							<xsl:value-of
								select="/ManualValidationReport/TestCaseReference/TestPlan" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Test Case Group</td>
						<td class="row3 ">
							<xsl:value-of
								select="/ManualValidationReport/TestCaseReference/TestGroup" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Test Case</td>
						<td class="row3">
							<xsl:value-of
								select="/ManualValidationReport/TestCaseReference/TestCase" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Test Step</td>
						<td class="row3 ">
							<xsl:value-of
								select="/ManualValidationReport/TestCaseReference/TestStep" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</xsl:template>


	<xsl:template match="SpecificReport">
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody>
					<tr class="border_bottom">
						<td class="row1 border_right" valign="top" rowspan="6">Result</td>
						<td class="row2 border_right dark-gray">
							<xsl:value-of select="/ManualValidationReport/SpecificReport/@Result" />
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
							<div style="width:100%;">
								<textarea readonly="true" style="width:100%" rows="10"
									wrap="off">
									<xsl:value-of select="/ManualValidationReport/SpecificReport/Comments" />
								</textarea>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

	</xsl:template>

</xsl:stylesheet>
