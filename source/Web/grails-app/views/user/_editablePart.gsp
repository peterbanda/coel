    <r:script>

		$(function(){
			$("#optionalDiv").hide()
			$("#hideOptionalButton").hide()
			$("#username").focus()			
		});

  		function showOptional() {
			$("#optionalDiv").show()
			$("#hideOptionalButton").show()
			$("#showOptionalButton").hide()
  		};

  		function hideOptional() {
			$("#optionalDiv").hide();
			$("#hideOptionalButton").hide()
			$("#showOptionalButton").show()
  		};

	</r:script>

		<ui:field bean="instance" name="firstName"/>
		<ui:field bean="instance" name="lastName"/>
		<ui:field bean="instance" name="email"/>
		<ui:field bean="instance" class="input-large" name="affiliation"/>
		<ui:field bean="instance" name="intendedUse">
			<ui:fieldInput>		
				<g:textArea class="input-xlarge" rows="6" name="intendedUse" value="${instance.intendedUse}" />
			</ui:fieldInput>
		</ui:field>
		
    	<div id="showOptionalButton" class="row">
    		<div class="pull-right">
       			<a href="javascript:void(0);" onclick="showOptional();">Show Optional Settings</a>
       		</div>
        </div>

    	<div id="hideOptionalButton" class="row">
    		<div class="pull-right">
       			<a href="javascript:void(0);" onclick="hideOptional();">Hide Optional Settings</a>
       		</div>
        </div>

		<div id="optionalDiv">
			<ui:field bean="instance" name="phoneNumber"/>
			<ui:field bean="instance" name="website"/>
			<ui:field bean="instance" name="address">
				<ui:fieldInput>		
					<g:textArea class="input-large" rows="4" name="address" value="${instance.address}" />
				</ui:fieldInput>
			</ui:field>
			<ui:field bean="instance" name="aboutMe">
				<ui:fieldInput>		
					<g:textArea class="input-xlarge" rows="6" name="aboutMe" value="${instance.aboutMe}" />
				</ui:fieldInput>
			</ui:field>
		</div>