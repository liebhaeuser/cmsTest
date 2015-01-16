<#macro printContent contentViewModel level=0>
	<h${level+2}>${contentViewModel.name} ${contentViewModel.children?size}</h${level+2}>
	<#list contentViewModel.properties?keys as key>
		${key}: ${contentViewModel.properties[key]}<br>
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
		<@printContent contentViewModel=contentViewModels.getContentViewModel("hello/world") />

		<h1>My Blog</h1>
		<#list contentViewModels.getContentViewModel("myBlog").children as blogEntry>
			<#assign properties=blogEntry.properties>
			<h2>${properties["jst:title"]}</h2>
			<p>${properties["jst:text"]}</p>
			<p>${properties["jst:creationDate"]}</p>
		</#list>
	</body>
</html>