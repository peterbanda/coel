<!DOCTYPE html>
<html>
    <theme:head>
    	<meta name="google-site-verification" content="h802p24iREDxFT6fQNizFamlw0b7auzti6JQOPfL6jc" />
    </theme:head>
    <theme:body>
        <theme:layoutTemplate name="header"/>
        <header class="masthead">
            <div class="inner">
                <div class="container">
                    <theme:layoutZone name="banner"/>
                </div>
            </div>  
        </header>
        <div class="container">
            <div class="content">
                <div class="row">
                    <div class="span11">
                        <theme:layoutZone name="body"/>
                    </div>
                </div>
                <div class="row">
                    <div class="span8">
                        <theme:layoutZone name="panel1"/>
                    </div>
                    <div class="span3">
                        <theme:layoutZone name="panel2"/>
                    </div>
                </div>
            </div>
            <theme:layoutTemplate name="footer"/>
            <div class="row">
            	<div class="pull-right">
					<ui:image uri="/images/psu_logo.png" width="300" alt="PSU"/>
    				<ui:image uri="/images/nsf_logo.png" width="166" alt="NSF"/>
    			</div>
    		</div>
        </div>
    </theme:body>
</html>