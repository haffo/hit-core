<xsl:stylesheet exclude-result-prefixes="map" version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:message="http://www.nist.gov/healthcare/validation/message"
	xmlns:report="http://www.nist.gov/healthcare/validation/message/hl7/v2/report"
	xmlns:map="urn:internal"
	xmlns:context="http://www.nist.gov/healthcare/validation/message/hl7/v2/context"
	xmlns:profile="http://www.nist.gov/healthcare/profile">
	<xsl:output method="html" omit-xml-declaration="yes" />
	<xsl:param name="withHeader">
		<xsl:value-of select="true()" />
	</xsl:param>

	<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />


	<xsl:template match="report:HL7V2MessageValidationReport">
		<xsl:apply-templates select="report:HeaderReport" />
		<xsl:apply-templates select="report:SpecificReport" />
	</xsl:template>

	<xsl:template match="report:HeaderReport">
		<script type="text/javascript">
					function toggle_visibility(id, elm) {
					var e =
					document.getElementById(id);
					if (elm.checked == true)
					e.style.display = '';
					else
					e.style.display = 'none';
					};
					function
					toggle_visibilityC(cls, elm) {
					var e =
					document.getElementsByClassName(cls);
					for(var i = e.length - 1; i >=
					0; i--) {
					if (elm.checked == true)
					e[i].style.display = '';
					else
					e[i].style.display = 'none';
					}
					};

					function fi(id) {
					var div =
					document.getElementById(id);
					if (div.style.display !== 'none') {
					document.getElementById("btn").childNodes[0].nodeValue = "View";
					div.style.display = 'none';
					}
					else {
					document.getElementById("btn").childNodes[0].nodeValue = "Hide";
					div.style.display = '';
					}
					};
		</script>
		<xsl:if test="$withHeader = boolean('true')">
			<div class="report-section">
				<table class="forumline title-background" width="100%"
					cellspacing="1" cellpadding="10">
					<tbody class="cf-tbody">
						<tr>
							<td class="row1 border_right">
								<span class="submaintitle2">Message Validation Report</span>
							</td>
							<td class="row2" style="font-weight:bold">
								<center>
									<xsl:call-template name="dateTransformer">
										<xsl:with-param name="myDate" select="message:DateOfTest" />
										<xsl:with-param name="myTime" select="message:TimeOfTest" />
									</xsl:call-template>
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
									<xsl:value-of select="message:Type" />
								</center>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="report-section">
				<table class="forumline" width="100%" cellspacing="1"
					cellpadding="2">
					<tbody class="cf-tbody border_right">
						<tr class="border_bottom">
							<td class="row1 border_right" valign="top" rowspan="2">Testing
								Tool</td>
							<td class="row2 border_right ">Name</td>
							<td class="row3 ">
								<xsl:value-of select="message:ServiceName" />
							</td>
						</tr>
						<tr class="border_bottom">
							<td class="row2 border_right ">Validation Version</td>
							<td class="row3 ">
								<xsl:value-of select="message:ServiceVersion" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<xsl:apply-templates select="message:TestCaseReference" />
		</xsl:if>
	</xsl:template>
	<xsl:template match="report:SpecificReport">
		<xsl:apply-templates select="report:MetaData/report:Profile" />
		<xsl:apply-templates select="report:MetaData/report:Message" />
		<xsl:apply-templates
			select="report:MetaData/report:Context/context:FailureInterpretation" />
		<xsl:call-template name="Summary" />
		<xsl:call-template name="Assertions">
			<xsl:with-param name="classification" select="'error'" />
			<xsl:with-param name="count"
				select="../report:HeaderReport/message:ErrorCount" />
			<xsl:with-param name="color" select="'red'" />
			<xsl:with-param name="msg" select="'Errors'" />
		</xsl:call-template>
		<xsl:call-template name="Assertions">
			<xsl:with-param name="classification" select="'alert'" />
			<xsl:with-param name="count"
				select="../report:HeaderReport/message:AlertCount" />
			<xsl:with-param name="color" select="'maroon'" />
			<xsl:with-param name="msg" select="'Alerts'" />
		</xsl:call-template>
		<xsl:call-template name="Assertions">
			<xsl:with-param name="classification" select="'warning'" />
			<xsl:with-param name="count"
				select="../report:HeaderReport/message:WarningCount" />
			<xsl:with-param name="color" select="'gold'" />
			<xsl:with-param name="msg" select="'Warnings'" />
		</xsl:call-template>
		<!-- <xsl:call-template name="Assertions"> <xsl:with-param name="classification" 
			select="'affirmative'" /> <xsl:with-param name="count" select="../report:HeaderReport/message:AffirmCount" 
			/> <xsl:with-param name="color" select="'green'" /> <xsl:with-param name="msg" 
			select="'Affirmatives'" /> </xsl:call-template> -->
		<!-- <xsl:call-template name="Assertions"> -->
		<!-- <xsl:with-param name="classification" select="'informational'"/> -->
		<!-- <xsl:with-param name="count" select="../report:HeaderReport/message:InfoCount"/> -->
		<!-- <xsl:with-param name="color" select="'blue'"/> -->
		<!-- <xsl:with-param name="msg" select="'Informationals'"/> -->
		<!-- </xsl:call-template> -->
	</xsl:template>
	<xsl:template name="Assertions">
		<xsl:param name="classification" />
		<xsl:param name="count" />
		<xsl:param name="color" />
		<xsl:param name="msg" />
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody>
					<tr class="row1">
						<td>
							Validation
							<xsl:value-of select="$msg" />
						</td>
						<td align="right">
							<xsl:attribute name="style">
								color : <xsl:value-of select='$color' />;
								font-weight: bold;
							</xsl:attribute>
							Count :
							<xsl:value-of select="$count" />
							<input type="checkbox">
								<xsl:attribute name="onclick">toggle_visibility('<xsl:value-of
									select="generate-id()" /><xsl:value-of
									select="$classification" />',this)</xsl:attribute>
								<xsl:if test="$classification='error'">
									<xsl:attribute name="checked">true</xsl:attribute>
								</xsl:if>
							</input>
						</td>
					</tr>
				</tbody>
			</table>
			<table class="forumline cf-report-category" width="100%"
				cellspacing="1" cellpadding="2">
				<xsl:if test="$classification!='error'">
						<xsl:attribute name="style">display : none;</xsl:attribute>
				</xsl:if>
					
				<xsl:attribute name="id">
					<xsl:value-of select='generate-id()' /><xsl:value-of select="$classification" />
				</xsl:attribute>
				
				<xsl:for-each-group select="./report:AssertionList/report:Assertion[@Result = $classification]" group-by="@Type">
					<xsl:call-template name="TmPT">
						<xsl:with-param name="id" select="generate-id(current-group()[1])"></xsl:with-param>
					</xsl:call-template>
				</xsl:for-each-group>						
			</table>
		</div>
	</xsl:template>
	<xsl:template name="TmPT">
		<xsl:param name="id"></xsl:param>
		<tbody>
			<tr>
				<td class="row5 border_bottom" colspan="3" style="width:30%">
					<xsl:value-of select="@Type" />
				</td>
				<td class="row5 border_bottom" align="right" style="width:70%">
					Count :
					<xsl:value-of select="count(current-group())" />
					<input type="checkbox" checked="true">
						<xsl:attribute name="onclick">toggle_visibilityC('<xsl:value-of
							select="$id" />',this)</xsl:attribute>
					</input>
				</td>
			</tr>
		</tbody>
		<xsl:for-each select="current-group()">
			<tbody class="border_bottom">
				<xsl:attribute name="class">alternate<xsl:value-of
					select="position() mod 2" /> border_bottom <xsl:value-of
							select="$id" /></xsl:attribute>
				<tr class="border_bottom">
					<td class="row3 border_right border_bottom" rowspan="5"
						style="width:20px;">
						<xsl:value-of select="position()" />
					</td>
					<td class="row3 border_right">Type :</td>
					<td class="row3 border_bottom" colspan="2">
						<xsl:value-of select="@Type" />
					</td>
				</tr>
				<tr class="border_bottom">
					<td class="row3 border_right">Description:</td>
					<td class="row3 border_bottom" colspan="2">
						<xsl:value-of select="report:Description" />
					</td>
				</tr>
				<tr class="row3 border_bottom">
					<td rowspan="3" class="border_right border_bottom" style="width:100px;">
						Location:</td>
					<xsl:if test="report:Location/report:Line">
						<td colspan="2" style="font-weight: bold">
							Line:
							<xsl:value-of select="report:Location/report:Line" />
						</td>
					</xsl:if>
				</tr>
				<xsl:if test="report:Location/report:Column">
					<tr class="row3 border_bottom">
						<td colspan="2" style="font-weight: bold">
							Column:
							<xsl:value-of select="report:Location/report:Column" />
						</td>
					</tr>
				</xsl:if>
				<xsl:if test="report:Location/report:Path">
					<tr class="row3 border_bottom">
						<td colspan="2" style="font-weight: bold;" class="border_bottom">
							Path:
							<xsl:value-of select="report:Location/report:Path" />
						</td>
					</tr>
				</xsl:if>
			</tbody>
		</xsl:for-each>

	</xsl:template>
	<xsl:template match="report:HeaderReport/message:TestCaseReference">
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody>
					<tr class="border_bottom">
						<td class="row1 border_right" valign="top" rowspan="4">Test Case</td>
						<td class="row2 border_right dark-gray">Test Plan</td>
						<td class="row3 ">
							<xsl:value-of select="report:TestPlan" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Test Group</td>
						<td class="row3 ">
							<xsl:value-of select="report:TestGroup" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Test Case</td>
						<td class="row3">
							<xsl:value-of select="report:TestCase" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Test Step</td>
						<td class="row3">
							<xsl:value-of select="report:TestStep" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="report:MetaData/report:Profile">
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody>
					<tr class="border_bottom">
						<td class="row1 border_right" valign="top" rowspan="6">Profile</td>
						<td class="row2 border_right dark-gray">Name</td>
						<td class="row3 ">
							<xsl:value-of select="@Name" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Organization</td>
						<td class="row3 ">
							<xsl:value-of select="@Organization" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Type</td>
						<td class="row3">
							<xsl:value-of select="@Type" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Profile Version</td>
						<td class="row3 ">
							<xsl:value-of select="@Version" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Profile Date</td>
						<td class="row3 ">
							<xsl:value-of select="@Date" />
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row2 border_right dark-gray">Standard</td>
						<td class="row3 ">
							<xsl:value-of select="@HL7Version" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="report:MetaData/report:Message">
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody>
					<tr class="row1">
						<th style="border-bottom:2pt #005C99 solid" align="left">Message</th>
					</tr>
					<tr class="border_bottom">
						<!-- <td class="row2 border_right dark-gray">Content</td> -->
						<td class="row2 border_right dark-gray ">
							<div style="text-align: center">
								<textarea style="width:100%;height:100%" readonly="true" wrap="off">
									<xsl:value-of select="report:Er7Message" />
								</textarea>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</xsl:template>
	<xsl:template name="Summary">
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tbody>
					<tr class="row1">
						<th style="border-bottom:2pt #005C99 solid" align="left">Summary</th>
					</tr>
					<tr class="border_bottom">
						<td class="row6 " style="color: red; font-weight: bold">							
							<input type="checkbox">
								<xsl:attribute name="onclick">toggle_visibility('<xsl:value-of
									select="generate-id()" />error',this)</xsl:attribute>
 								<xsl:attribute name="checked">true</xsl:attribute>
 							</input>
							
							<xsl:value-of select="../report:HeaderReport/message:ErrorCount" />
							Errors
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row6 " style="color: maroon; font-weight: bold">
 							<input type="checkbox">
								<xsl:attribute name="onclick">toggle_visibility('<xsl:value-of
									select="generate-id()" />alert',this)</xsl:attribute>
  							</input>
 							
							<xsl:value-of select="../report:HeaderReport/message:AlertCount" />
							Alerts
						</td>
					</tr>
					<tr class="border_bottom">
						<td class="row6 " style="color: gold; font-weight: bold">
 							<input type="checkbox">
								<xsl:attribute name="onclick">toggle_visibility('<xsl:value-of
									select="generate-id()" />warning',this)</xsl:attribute>
  							</input>
							<xsl:value-of select="../report:HeaderReport/message:WarningCount" />
							Warnings
						</td>
					</tr>
					<!-- <tr class="border_bottom"> <td class="row6" style="color: green; 
						font-weight: bold"> <input type="checkbox" onclick="toggle_visibility('affirmative',this)" 
						/> <xsl:value-of select="../report:HeaderReport/message:AffirmCount" /> Affirmatives 
						</td> </tr> -->
				</tbody>
			</table>
		</div>
	</xsl:template>
	<xsl:template
		match="report:MetaData/report:Context/context:FailureInterpretation">
		<div class="report-section">
			<table class="forumline" width="100%" cellspacing="1"
				cellpadding="2">
				<tr class="row1">
					<th style="border-bottom:2pt #005C99 solid" align="left">Failures
						interpretation</th>
					<td align="right" style="border-bottom:2pt #005C99 solid">
						<button id="btn" onclick="fi('mfi')"> 
						<xsl:attribute name="onclick">
							fi('mfi<xsl:value-of select="generate-id()" />')
						</xsl:attribute>
						View </button>
					</td>
				</tr>
				<tbody style="display : none;">
					<xsl:attribute name="id">mfi<xsl:value-of select="generate-id()" /></xsl:attribute>
					<tr>
						<td class="row5 border_bottom border_right" style="width:50%">Category</td>
						<td class="row5 border_bottom" style="width:50%">Classification</td>
					</tr>
					<xsl:for-each select="context:MessageFailure">
						<tr>
							<xsl:attribute name="class">border_bottom alternate<xsl:value-of
								select="position() mod 2" /></xsl:attribute>
							<td class="border_right" style="width:50%">
								<xsl:value-of select="@Type" />
							</td>
							<td style="width:50%">
								<xsl:value-of select="@Result" />
							</td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>
		</div>
	</xsl:template>
	<xsl:template name="dateTransformer">
		<xsl:param name="myDate" />
		<xsl:param name="myTime" />
		<xsl:variable name="year" select="substring-before($myDate, '-')" />
		<xsl:variable name="month"
			select="substring-before(substring-after($myDate, '-'), '-')" />
		<xsl:variable name="day"
			select="substring-before(substring-after(substring-after($myDate, '-'), '-'), '-')" />
		<xsl:value-of select="$month" />
		<xsl:text> </xsl:text>
		<xsl:value-of select="$day" />
		<xsl:text> </xsl:text>
		<xsl:text> </xsl:text>
		<xsl:value-of select="$year" />
		<xsl:text>, </xsl:text>
		<xsl:value-of select="$myTime" />
	</xsl:template>

	<xsl:template name="segmentBreaker">
		<xsl:param name="segment" />
		<xsl:if test="string-length($segment) > 0">
			<xsl:value-of select="substring($segment,1,90)" />
			<br />
			<xsl:variable name="segment">
				<xsl:value-of select="substring($segment,91)" />
			</xsl:variable>
			<xsl:call-template name="segmentBreaker">
				<xsl:with-param name="segment" select="$segment" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
