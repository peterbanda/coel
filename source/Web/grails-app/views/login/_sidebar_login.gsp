  					<g:if test='${flash.error_message}'>
						<div class="errors" style="width:400px">
							<ul>
					      		<li>${flash.error_message}</li> 
  							</ul>
						</div>
					</g:if>
 <div id="sidebar" style="width: 170px">
	<h3>Login</h3>
    	<form method="POST" action="${resource(file: 'j_spring_security_check')}">
    		<table>
    			<tr>
    				<td>
    					<label for='username'>Username</label>
    				</td>
    				<td>
						<input style='width: 80px;' type='text' class='text_' name='j_username' id='username' />
					</td>
				</tr>
    			<tr>
    				<td>
						<label for='password'>Password</label>
					</td>
    				<td>
						<input style='width: 80px;' type='password' class='text_' name='j_password' id='password' />
					</td>
				</tr>
    			<tr>
    				<td>
						<label for='remember_me'>Remember me</label>
					</td>
    				<td>						
						<input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
						<g:if test='${hasCookie}'>checked='checked'</g:if> />
					</td>
				</tr>
			</table>
			<input type='submit' value='Login' />
    		
<!--
        	<p>
				<label for='username'>Username</label>
				<input style='width: 80px;' type='text' class='text_' name='j_username' id='username' />
			</p>
			<p>
				<label for='password'>Password</label>
				<input style='width: 80px;' type='password' class='text_' name='j_password' id='password' />
			</p>
			<p>
				<label for='remember_me'>Remember me</label>
				<input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
				<g:if test='${hasCookie}'>checked='checked'</g:if> />
			</p>
			<p>
				<input type='submit' value='Login' />
			</p>
-->
        </form>
</div>