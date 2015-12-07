<html>
<head>
    <nav:set path="app"/>
    <theme:title>Posts</theme:title>
	<theme:layout name="main"/>
<head>
<body>
    <theme:zone name="body">
      <div class="accordion" id="accordion">
        <g:each in="${list}" var="post" >
            <div class="accordion-group" style="margin-bottom: 22px">
                <div class="accordion-heading">
                    <div class="row-fluid spacedLeft">
                        <ul class="inline spacedLeft">
                            <li>
                                <ui:h3>${post.title}</ui:h3>
                            </li>
                            <li>
                                posted on <g:formatDate format="yyyy-MM-dd hh:mm:ss" date="${post.dateCreated}"/>
                            </li>
                        </ul>
                    </div>
                </div>
                <div id="collapseOne" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        ${post.content}
                    </div>
                </div>
            </div>
            <div class="row-fluid style:height = 20px">

            </div>
        </g:each>
      </div>
    </theme:zone>
</body>
</html>