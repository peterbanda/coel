<sec:ifAnyGranted roles="ROLE_ADMIN">
	<ul class="nav">
        <li class="dropdown">
			<a data-toggle="dropdown" class="dropdown-toggle">
				<span>                                  
					Admin Zone
					<i class="caret"></i>
				</span>
			</a>
			<ul class="dropdown-menu">
				<li>
					<gui:actionLink controller="user" action="list" icon="icon-user">
						<span>Users</span>
					</gui:actionLink>
				</li>
				<li>
					<gui:actionLink controller="role" action="list" icon="icon-user">
						<span>Roles</span>
					</gui:actionLink>
				</li>
		        <li>
					<gui:actionLink controller="post" action="list" icon="icon-book">
						<span>Posts</span>
					</gui:actionLink>
        		</li>
		        <li>
					<gui:actionLink controller="admin" action="grid" icon="icon-wrench">
						<span>Grid</span>
					</gui:actionLink>
        		</li>
			</ul>
		</li>
	</ul>
</sec:ifAnyGranted>	