<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils"%>
<%@ page import="com.banda.core.domain.um.User" %>

<sec:ifLoggedIn>
	<r:script>
		$.get('${createLink(controller: "user", action: "getLoggedUserEmailMD5")}',
			function(data) {
				$('#gravatarDiv').html('<img src="https://www.gravatar.com/avatar/' + data + '?s=25&amp;d=mm&amp;r=g">');
			});
	</r:script>
</sec:ifLoggedIn>

<ul class="nav">
		<sec:ifLoggedIn>
			<li>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown user-avatar">
                    <a data-toggle="dropdown" class="dropdown-toggle">
                        <div>
                        	<span id="gravatarDiv">
                            	
                            </span>
                            <span>
                            	${sec.username()}  	              
                                <i class="caret"></i>
                            </span>
                        </div>
                    </a><ul class="dropdown-menu">
                    <li>
						<gui:actionLink controller="user" action="editLoggedUser" icon="icon-user">
							<span>Edit Profile</span>
						</gui:actionLink>
                    </li>
                    <li>
						<gui:actionLink controller="user" action="changePasswordLoggedUser" icon="icon-wrench">
							<span>Update Password</span>
						</gui:actionLink>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="/logout">
                        	<i class="glyphicon glyphicon-log-out"></i>
                            <span>Log out</span>
                        </a>
                    </li>
                </ul>
                </li>
            </ul>
            </li>
       	</sec:ifLoggedIn>
		<sec:ifNotLoggedIn>
			<li>
				<a href="/login">
                	<span>Login</span>
				</a>
			</li>
			<li>
				<a href="${g.createLink(controller:'user', action: 'register')}">
                	<span>Register</span>
				</a>
			</li>
       	</sec:ifNotLoggedIn>
</ul>