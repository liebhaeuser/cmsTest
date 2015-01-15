<#macro printContent contentViewModel level=0>
	<h${level+2}>${contentViewModel.path}</h${level+2}>

	<#list contentViewModel.properties as property>
		${property.name}: ${property.value}<br>
	</#list>
	
	<#if (contentViewModel.children?size > 0) >
		<#list contentViewModel.children as child>
			<@printContent contentViewModel=child level=level+1 />
		</#list>
	</#if>
</#macro>

<!DOCTYPE html>
<html>
	<body>
		<h1>Jackrabbit Test</h1>
		<p>${message}</p>
		<@printContent contentViewModel=contentViewModel />
	</body>
</html>