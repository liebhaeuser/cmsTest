package org.jstage.cms;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
@ComponentScan(basePackages = {"org.jstage.cms"})
public class SampleController {
	
	@Resource
	private ContentService contentService;

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", "Hello CMS world!");
		return "welcome";
	}
	
	@RequestMapping("/createNodeType")
	@ResponseBody
	public String createNodeType() throws RepositoryException {
		contentService.createBlogNodeType();
		return ("creation successfull");
	}
	
	@RequestMapping("/createContent")
	@ResponseBody
	public String createContent() throws RepositoryException {
		try {
			contentService.createContent();
		} catch (RepositoryException e) {
			e.printStackTrace();
			return "Content Creation Failed";
		}

		return "Content Created!";
	}
	
	@RequestMapping("/test")
	public String test(Map<String, Object> model) throws RepositoryException {
		StringBuffer strBuffer = new StringBuffer();
		//strBuffer.append(createContent());
		strBuffer.append("<br>");
		ContentViewModels contentViewModels = new ContentViewModels();
		contentViewModels.setContentService(contentService);
		model.put("contentViewModels", contentViewModels);
		//strBuffer.append(removeContent());
		model.put("message", strBuffer.toString());
		return "test";
	}
	
	private String removeContent() {
		try {
			contentService.removeContent();
		} catch (RepositoryException e) {
			e.printStackTrace();
			return "Removing Content Failed";
		}
		
		return "Content Removed!";
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleController.class, args);
	}

}
