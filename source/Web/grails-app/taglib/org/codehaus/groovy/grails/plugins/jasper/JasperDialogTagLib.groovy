/* Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.codehaus.groovy.grails.plugins.jasper

/* 
 * @author mfpereira 2007
 */
class JasperDialogTagLib {

    JasperService jasperService

    def static requiredAttrs = ['jasper','format']

// tags existing as of plugin version 0.9
    def jasperDialogReport = {attrs, body ->
        validateAttributes(attrs)
        String jasperName = attrs['jasper']
        String jasperNameNoPunct = jasperName.replaceAll(/[^a-zA-Z0-9]/, '')
        String appPath = grailsAttributes.getApplicationUri(request)
        String webAppPath = appPath + pluginContextPath
        String idAttr = attrs['id'] ?: ""
        String reportName = attrs['name'] ?: ""
        String delimiter = attrs['delimiter'] ?: "|"
        String delimiterBefore = attrs['delimiterBefore'] ?: delimiter
        String delimiterAfter = attrs['delimiterAfter'] ?: delimiterBefore
        String description = attrs['description'] ?: (reportName ? "<strong>${reportName}</strong>" : "")
        String formClass = attrs['class'] ?: "jasperReport"
        String buttonClass = attrs['buttonClass'] ?: "jasperButton"
        String heightAttr = attrs['height'] ? ' height="' + attrs['height'] + '"' : '' // leading space on purpose
        boolean buttonsBelowBody = (attrs['buttonPosition'] ?: 'top') == 'bottom'

        String controller = attrs['controller'] ?: "jasper"
        String action = attrs['action'] ?: "";

        if (body()) {
            // The tag has a body that, presumably, includes input field(s), so we need to wrap it in a form
			out << """<div id="jasper-export-dialog" title="Export as">"""
            out << """<form class="${formClass}"${idAttr ? ' id="' + idAttr + '"' : ''} name="${jasperName}" action="${appPath}/${controller}/${action}">"""
            out << """<input type="hidden" name="_name" value="${reportName}" />
					  <input type="hidden" name="_file" value="${jasperName}" />"""

            if (attrs['inline']) {
                out << """<input type="hidden" name="_inline" value="${attrs['inline']}" />\n"""
            }

            if (buttonsBelowBody) {
                out << description << body()
				out << """<select id="_format" name="_format">"""
                out << renderButtons(attrs, buttonClass, jasperNameNoPunct, webAppPath)
				out << "</select>"
            } else {
				out << """<select id="_format" name="_format">"""
                out << renderButtons(attrs, buttonClass, jasperNameNoPunct, webAppPath)
				out << "</select>"
                out << body()
            }
            out << "</form>"
        } else {
            /*
             * The tag has no body, so we don't need a whole form, just a link.
             * Note that GSP processing is not recursive, so we cannot use a g:link here.  It has to already be an A tag.
             */
			out << """<select id="_format" name="_format">"""
            String result = delimiterBefore
            attrs['format'].toUpperCase().split(",").eachWithIndex {it, i ->
                if (i > 0) result += delimiter
				result += """<option value="${it.trim()}" data-imagesrc="${webAppPath}/images/icons/${it.trim()}.gif"${heightAttr} data-description="${it.trim()}">${it.trim()}</option>"""
            }
            result += delimiterAfter+' '+description
            out << result
			out << "</select>"
        }
		out << "</div>"
    }

    def addReportParameter = {attrs, body ->
    		def parameterName = attrs['name']
    		def parameterType = attrs['type']
    		out << parameterName + parameterType
    }

	def jasperDialogReportScript = {attrs, body ->
		String idAttr = attrs['id'] ?: ""

		out <<
        """
      <r:script>
			\$(function(){

				\$( "#dialog:ui-dialog" ).dialog( "destroy" );

				\$( "#jasper-export-dialog" ).dialog({
					autoOpen: false,
					resizable: true,
					height:320,
					width:170,
					modal: true,
					buttons: {
						Submit: function() {
							\$('#_format').ddslick('destroy');
							\$("#${idAttr}").submit();
							\$( this ).dialog( "close" );
						},
						Cancel: function() {
							\$( this ).dialog( "close" );
						}
					}
				});	
			});

			function showJasperDialog() {
		    	\$('#_format').ddslick({
		    		width: 90,
		    		imagePosition: "left",
		    		selectText: "Select export format",
		    	});
				\$( "#jasper-export-dialog" ).dialog( "open" );
			}
      </r:script>
        """
    }

    protected String renderButtons(attrs, buttonClass, jasperNameNoPunct, webAppPath) {
        String result = ""
        attrs['format'].toUpperCase().split(",").eachWithIndex {it, i ->
			result += """<option value="${it.trim()}" data-imagesrc="${webAppPath}/images/icons/${it.trim()}.gif" data-description="${it.trim()}">${it.trim()}</option>"""
        }
        return result
    }

    private void validateAttributes(attrs) {
      JasperDialogTagLib.requiredAttrs.each {attrName ->
          if (!attrs[attrName]) {
            // TODO more appropriate Exception type
            throw new Exception(message(code:"jasper.taglib.missingAttribute", args:["${attrName}", "${JasperDialogTagLib.requiredAttrs.join(', ')}"]))
          }
      }
        //Verify the 'format' attribute
        def availableFormats = ["PDF", "HTML", "XML", "CSV", "XLS", "RTF", "TEXT","ODT","ODS","DOCX","XLSX","PPTX"]
        attrs.format.toUpperCase().split(",").each {
            if (!availableFormats.contains(it.trim())) {
                // TODO more appropriate Exception type
                throw new Exception(message(code: "jasper.taglib.invalidFormatAttribute", args: ["${it}", "${availableFormats}"]))
            }
        }
    }
}