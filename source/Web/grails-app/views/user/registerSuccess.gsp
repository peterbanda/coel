<html>
<head>
	<theme:title>Registration Completed</theme:title>
    <nav:set path="app"/>
    <theme:layout name="main"/>
</head>
<body>
	<theme:zone name="body">
		<div class="alert alert-success" role="alert">
			<p>${instance.firstName}, well done! Your registration is completed.</p>
			<p>We will now review your application and let you know if it is approved, in which case your account will be activated.
			   Our decision is based mainly on your intended use of COEL, i.e., whether you fit into the research/education category COEL is primarily provided for.
			   Either way, you will receive an email at ${instance.email}.</p>
			<p>Thank you for your interest.</p>
		</div>
    </theme:zone>
</body>
</html>