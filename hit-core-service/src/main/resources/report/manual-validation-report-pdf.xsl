<xsl:stylesheet exclude-result-prefixes="map" version="2.0"
	xmlns:manualvalidationreport="http://www.nist.gov/healthcare/validation/manual/report"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:map="urn:internal">
	<xsl:output method="html" />
	<xsl:param name="withHeader">
		<xsl:value-of select="true()" />
	</xsl:param>

	<xsl:param name="withManualBody">
		<xsl:value-of select="true()" />
	</xsl:param>


	<xsl:template match="manualvalidationreport:ManualValidationReport">
		<xsl:apply-templates select="manualvalidationreport:HeaderReport" />
		<xsl:apply-templates select="manualvalidationreport:TestCaseReference" />
		<xsl:apply-templates select="manualvalidationreport:SpecificReport" />
	</xsl:template>
	<xsl:template match="manualvalidationreport:HeaderReport">
		<xsl:if test="$withHeader = boolean('true')">
			<div class="report-section">
				<table class="forumline title-background" width="100%"
					cellspacing="1" cellpadding="10">
					<tbody class="cf-tbody">
						<tr>
							<td class="row1 border_right">
								<span class="submaintitle2">Manual Validation Report</span>
							</td>
							<td class="row2" style="font-weight:bold">
								<center>
									<xsl:value-of select="manualvalidationreport:DateOfTest" />
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
									<xsl:value-of select="manualvalidationreport:ValidationType" />
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
									<xsl:value-of select="manualvalidationreport:Type" />
								</center>
							</td>
						</tr>
					</tbody>
				</table>
			</div>

			<!-- <xsl:apply-templates select="message:TestCaseReference" /> -->

		</xsl:if>

	</xsl:template>

	<xsl:template match="manualvalidationreport:TestCaseReference">
		<xsl:if test="$withHeader = boolean('true')">
			<div class="report-section">


				<table class="forumline" width="100%" cellspacing="1"
					cellpadding="2">
					<tbody>
						<tr class="border_bottom">
							<td class="row1 border_right" valign="top" rowspan="4">Test Case</td>
							<td class="row2 border_right dark-gray">Test Plan</td>
							<td class="row3 ">
								<xsl:value-of select="manualvalidationreport:TestPlan" />
							</td>
						</tr>
						<tr class="border_bottom">
							<td class="row2 border_right dark-gray">Test Group</td>
							<td class="row3 ">
								<xsl:value-of select="manualvalidationreport:TestGroup" />
							</td>
						</tr>
						<tr class="border_bottom">
							<td class="row2 border_right dark-gray">Test Case</td>
							<td class="row3">
								<xsl:value-of select="manualvalidationreport:TestCase" />
							</td>
						</tr>
						<tr class="border_bottom">
							<td class="row2 border_right dark-gray">Test Step</td>
							<td class="row3">
								<xsl:value-of select="manualvalidationreport:TestStep" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</xsl:if>
	</xsl:template>


	<xsl:template match="manualvalidationreport:SpecificReport">
		<xsl:if test="$withManualBody = boolean('true')">
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
								<xsl:for-each select="tokenize(manualvalidationreport:Comments,'\r')">
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

</xsl:if>

	</xsl:template>

	<xsl:template name="commentsBreaker">
		<xsl:param name="commentLine" />
		<xsl:if test="string-length($commentLine) > 0">
			<xsl:value-of select="substring($commentLine,1,90)" />
			<br />
			<xsl:variable name="commentLine">
				<xsl:value-of select="substring($commentLine,91)" />
			</xsl:variable>
			<xsl:call-template name="commentsBreaker">
				<xsl:with-param name="commentLine" select="$commentLine" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>



</xsl:stylesheet>
