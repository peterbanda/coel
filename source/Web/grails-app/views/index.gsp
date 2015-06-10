<html>
<head>
	<theme:layout name="home"/>
	<theme:title text="home.page.title"></theme:title>
	<nav:set path="app"/>
<head>
<body>  
	<theme:zone name="body">
		<div class="row-fluid">
		<div class="hero-unit span9">
			<ui:h1>Welcome to COEL</ui:h1>
			<ui:h4 text="(Collective Cellular Computing Project)"/>
			<br/>
			<p>
			COEL is a web-based chemistry and complex network simulation framework. COEL's most prominent features include ODE-based simulations
			of chemical reaction networks and multicompartment reaction networks, with rich options for user interactions with those networks.
			COEL provides DNA-strand displacement transformations and visualization (and is to our knowledge the first CRN framework to do so),
			GA optimization of rate constants, expression validation, an application-wide plotting engine, and SBML/Octave/Matlab export.
			COEL's visually pleasing and intuitive user interface, simulations that run on a large computational grid, reliable database storage, and transactional services make 
			it ideal for collaborative research and education.
			<br/>
			Besides chemical reaction networks, COEL provides a unified and extendible environment for the definition
			and manipulation of complex Boolean and real-valued networks.
			</p>
			<p>
			COEL framework is built on <a href="https://grails.org">Grails</a>, <a href="https://spring.io">Spring</a>, <a href="http://hibernate.org/">Hibernate</a>, and <a href="http://www.gridgain.com">GridGain</a> technology stack.
			</p>
			<p>Brief documentation can be found <a href="http://arxiv.org/abs/1407.4027">here</a>.</p>
			<p>Have fun!</p>
		</div>
		<div class="thumbnail span3">
			<div class="caption">
				<ui:h3 text="Blog (News)"/>
				<hr>
				<ul>
					<g:if test="${posts.size() == 0}">
						<i>None</i>
					</g:if>
					<g:else>
						<g:each in="${posts}" var="post" >
							<li style="margin-bottom: 10px">
								<g:link controller="post" action="listView">
									<b>${post.title}</b><br>On <g:formatDate format="yyyy-MM-dd" date="${post.dateCreated}"/>
								</g:link>
							</li>
						</g:each>
					</g:else>
				</ul>
			</div>
		</div>
		</div>
	</theme:zone>
	<theme:zone name="banner">
		<ui:carousel>
			<ui:slide active="${true}">
				<a class="brand" href="${g.createLink(uri:'/')}">
                   	<ui:image uri="/images/Coel-logo.png" alt="COEL"/>
				</a>
			</ui:slide>
			<ui:slide>
				<ui:image uri="/images/coel-dna.png" alt="chemistry"/>
				<div class="carousel-caption span6">
					<h1>Model Chemistry</h1>
					<p><a href="${g.createLink(uri:'/ac')}" class="btn btn-large btn-success">Get Started</a></p>
      			</div>
			</ui:slide>
			<ui:slide>
				<ui:image uri="/images/coel-net.png" alt="Network"/>
				<div class="carousel-caption span6">
					<h1 class="muted text-center">Simulate Networks</h1>
					<p><a href="${g.createLink(uri:'/network')}" class="btn btn-large btn-success">Get Started</a></p>
	            </div>
			</ui:slide>
			<ui:slide>
				<ui:image uri="/images/coel-evo.png" alt="Evolution"/>
                <div class="carousel-caption span6">
					<h1 class="muted text-center">Evolve</h1>
	                <p><a href="${g.createLink(uri:'/evo')}" class="btn btn-large btn-success">Get Started</a></p>
				</div>
			</ui:slide>
        </ui:carousel>
	</theme:zone>

	<theme:zone name="panel1">
		<ui:h3 text="Try It"/>
		<p>We provide a free account for selected research teams. If you wish to collaborate in the areas of (bio)chemical computing, reservoir computing, or neural networks, please contact us or <a href="/user/register">register</a>.</p>
		<p>We are also looking for beta testers and web designers to help us preparing the next 0.9.0 release. For bug reporting, request a Jira account <a href="http://linth.ece.pdx.edu:8090">here</a>. Your feedback is valuable for us!</p>
	</theme:zone>

	<theme:zone name="panel2">
		<div class="row-fluid">
            <div class="offset2">
		        <ui:h3 text="Contact"/>
		        <address>
			        <strong>Peter Banda</strong><br>
			        <i class="icon-envelope"></i> <a href="mailto:banda@pdx.edu">banda@pdx.edu</a><br>
  			        <i class="icon-chevron-right"></i> <a href="http://peterbanda.net">peterbanda.net</a><br>
		        </address>
		        <address>
    			    <strong>Teuscher Lab</strong><br>
	    	   	    Portland State University<br>
  		    	    <i class=" icon-chevron-right"></i> <a href="http://www.teuscher-lab.com">teuscher-lab.com</a><br>
		        </address>
            </div>
		</div>
	</theme:zone>
</body>
</html>