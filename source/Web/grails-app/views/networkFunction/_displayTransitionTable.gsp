<%@ page import="org.apache.commons.lang.StringUtils" %>
<g:set var="binaryOutput" value="${StringUtils.join(it?.outputs.collect{ output -> if (output) 1 else 0}, "")}" />

<r:script>
	$(function(){
		updateStringsForBinary();
		if (!${editable}) {
		    $("#binaryValue").attr("readonly", "readonly");
		    $("#decimalValue").attr("readonly", "readonly");
		    $("#hexadecimalValue").attr("readonly", "readonly");
		}
	});

    function reverseOrder() {
        $("#binaryValue").val($("#binaryValue").val().split('').reverse().join(''));
        updateStringsForBinary();

        if ($("#lsbFirst").val() == "false") {
            $("#lsbFirst").val("true");
            $("#bitOderValue").html("Least Significant Bit First");
        } else {
            $("#lsbFirst").val("false");
            $("#bitOderValue").html("Most Significant Bit First");
        }
    };

    function updateStringsForBinary() {
	    $.getJSON('${createLink(action: "getRepresentationsForBinaryString")}?string=' + $("#binaryValue").val(),updateStrings);
  	};

    function updateStringsForDecimal() {
	    $.getJSON('${createLink(action: "getRepresentationsForDecimalString")}?string=' + $("#decimalValue").val(),updateStrings);
  	};

    function updateStringsForHexadecimal() {
	    $.getJSON('${createLink(action: "getRepresentationsForHexadecimalString")}?string=' + $("#hexadecimalValue").val(),updateStrings);
  	};

    function updateStrings(data) {
		$("#binaryValue").val(data.binaryString);
		$("#decimalValue").val(data.decimalString);
		$("#hexadecimalValue").val(data.hexadecimalString);
  	};

</r:script>

<div class="row-fluid thumbnail">
    <g:hiddenField name="lsbFirst" value="${true}" />
    <div class="spacedTop">
        <div id="showAdvancedButton" class="pull-right">
            <a href="javascript:void(0);" onclick="reverseOrder();">Reverse Order</a>
        </div>
    </div>
    <div class="form-horizontal form-dense">
        <ui:field label="Bit Order">
            <ui:fieldInput>
                <div id="bitOderValue" class="spacedTop">
                    Least Significant Bit First
                </div>
            </ui:fieldInput>
        </ui:field>
        <ui:field label="Binary">
            <ui:fieldInput>
                <g:textArea name="binaryValue" rows="3" class="span11" value="${binaryOutput}" onkeyup="updateStringsForBinary();"/>
            </ui:fieldInput>
        </ui:field>
        <ui:field label="Decimal">
            <ui:fieldInput>
                <g:textArea name="decimalValue" rows="2" class="span11" value="" onkeyup="updateStringsForDecimal();"/>
            </ui:fieldInput>
        </ui:field>
        <ui:field label="Hexadecimal">
            <ui:fieldInput>
                <g:textArea name="hexadecimalValue" rows="2" class="span11" value="" onkeyup="updateStringsForHexadecimal();"/>
            </ui:fieldInput>
        </ui:field>
    </div>
</div>