<%@ page import="org.apache.commons.lang.StringUtils" %>
<g:set var="binaryOutput" value="${StringUtils.join(it.outputs.collect{ output -> if (output) 1 else 0}, "")}" />
<g:set var="decimalOutput" value="${new BigInteger(binaryOutput, 2).toString()}" />
<g:set var="hexadecimalOutput" value="${new BigInteger(binaryOutput, 2).toString(16)}" />

<g:set var="reversedBinaryOutput" value="${binaryOutput.reverse()}" />
<g:set var="reversedDecimalOutput" value="${new BigInteger(reversedBinaryOutput, 2).toString()}" />
<g:set var="reversedHexadecimalOutput" value="${new BigInteger(reversedBinaryOutput, 2).toString(16)}" />

<r:script>

    var lsbFirst = true;

    function reverseOrder() {
        lsbFirst = !lsbFirst;
        if (lsbFirst) {
            $("#bitOderValue").html("Least Significant Bit First");
            $("#binaryValue").val("${binaryOutput}");
            $("#decimalValue").val("${decimalOutput}");
            $("#hexadecimalValue").val("${hexadecimalOutput}");
        } else {
            $("#bitOderValue").html("Most Significant Bit First");
            $("#binaryValue").val("${reversedBinaryOutput}");
            $("#decimalValue").val("${reversedDecimalOutput}");
            $("#hexadecimalValue").val("${reversedHexadecimalOutput}");
        }
    };

</r:script>

<div class="row-fluid thumbnail">
    <div class="spacedTop">
        <div id="showAdvancedButton" class="pull-right">
            <a href="javascript:void(0);" onclick="reverseOrder();">Reverse Order</a>
        </div>
    </div>
    <div class="form-horizontal form-dense">
        <f:display bean="${it}" label="Bit Order" property="id">
            <div id="bitOderValue">
                Least Significant Bit First
            </div>
        </f:display>
        <f:display bean="${it}" label="Binary" property="id">
            <g:textArea name="binaryValue" rows="3" class="span11" value="${binaryOutput}" readonly="readonly"/>
        </f:display>

        <f:display bean="${it}" label="Decimal" property="id">
            <g:textArea name="decimalValue" rows="2" class="span11" value="${decimalOutput}" readonly="readonly"/>
        </f:display>

        <f:display bean="${it}" label="Hexadecimal" property="id">
            <g:textArea name="hexadecimalValue" rows="2" class="span11" value="${hexadecimalOutput}" readonly="readonly"/>
        </f:display>
    </div>
</div>