package edu.banda.coel.web

class AsyncCometTagLib {

	static namespace = 'asynccomet'

    def register = { attrs ->
		String controllerName = attrs['controller']
		String cometFrameName = attrs['comet-frame'] ?: "cometFrame"
    	out << """
		<script type="text/javascript">
			var count = 0;

			\$(function(){
				var url = window.context + '/${controllerName}/listen?' + count;
				\$('#${cometFrameName}').prop('src',url);
				count++;
			});
		</script>
		"""
		//				var context = '\${request.contextPath}';
    }
}