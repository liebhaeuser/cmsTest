package org.jstage.cms;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;

import org.apache.jackrabbit.commons.JcrUtils;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

	private Repository repository;

	private Session session;

	@PostConstruct
	private void init() throws RepositoryException, MalformedURLException {
		//repository = new URLRemoteRepository("http://localhost:8080/rmi");
		repository = JcrUtils.getRepository();
		session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
	}

	@PreDestroy
	private void cleanUp() {
		session.logout();
	}
	
	public void createBlogNodeType() throws RepositoryException {
		NodeTypeManager nodeTypeManager = session.getWorkspace().getNodeTypeManager();
		NamespaceRegistry ns = session.getWorkspace().getNamespaceRegistry();
		ns.registerNamespace("jst", "http://www.jstage.org/CustomNodeType");

		// Create node type
		NodeTypeTemplate nodeTypeTemplate = nodeTypeManager.createNodeTypeTemplate();
		nodeTypeTemplate.setName("jst:blogNodeType");

		// Create a new property
		PropertyDefinitionTemplate customProperty1 = nodeTypeManager.createPropertyDefinitionTemplate();
		customProperty1.setName("jst:title");
		customProperty1.setRequiredType(PropertyType.STRING);
		PropertyDefinitionTemplate customProperty2 = nodeTypeManager.createPropertyDefinitionTemplate();
		customProperty2.setName("jst:text");
		customProperty2.setRequiredType(PropertyType.STRING);
		PropertyDefinitionTemplate customProperty3 = nodeTypeManager.createPropertyDefinitionTemplate();
		customProperty3.setName("jst:creationDate");
		customProperty3.setRequiredType(PropertyType.DATE);
		// Add property to node type
		nodeTypeTemplate.getPropertyDefinitionTemplates().add(customProperty1);
		nodeTypeTemplate.getPropertyDefinitionTemplates().add(customProperty2);
		nodeTypeTemplate.getPropertyDefinitionTemplates().add(customProperty3);
		/* Register node type */
		nodeTypeManager.registerNodeType(nodeTypeTemplate, true);
		session.save();
	}

	public void createContent() throws RepositoryException {
		Node root = session.getRootNode();

		// Store content
		Node hello = root.addNode("hello");
		Node world = hello.addNode("world");
		world.setProperty("message", "Hello, World!");
		world.setProperty("creationDate", Calendar.getInstance());

		Node myBlog = root.addNode("myBlog");
		Node entry1 = myBlog.addNode("firstEntry", "jst:blogNodeType");
		entry1.setProperty("jst:title", "Neues iSYS JIRA Projekt \"jStage Hama Support\"");
		entry1.setProperty(
				"jst:text",
				"<p>Im iSYS JIRA gibt es ein neues Projekt &quot;<a href=\"https://tools.isys-software.de/jira/browse/JHS\">jStage Hama Support</a>&quot;. In diesem Projekt sollen in Zukunft alle Anfragen seitens Hama, die nicht einem bestehenden Vorgang zugeordnet werden k&ouml;nnen eingetragen werden, falls die Zeit, die Ihr f&uuml;r die Bearbeitung braucht &gt;= 15 Minuten betr&auml;gt.</p>"
						+ "<ol>"
						+ "<li>Zeit &gt;= 15 Minuten</li>"
						+ "<li>Neuer JIRA Eintrag: Wer? Wann? Was?</li>"
						+ "<li>L&ouml;sung dokumentieren</li>"
						+ "<li>Ggf. neuer JIRA Eintrag im HIS oder DHW Projekt, falls das Problem auf Fehler in der Software zur&uuml;ck zu f&uuml;hren ist</li></ol>"
						+ "<p>Bei Anfragen per Email bitte den Inhalt der Email mit ablegen. Zu den Anfragen z&auml;hlen beispielsweise:</p>"
						+ "<ul>"
						+ "<li>Anfragen zur Sichtbarkeit von Artikeln oder Kategorien</li>"
						+ "<li>Probleme mit Rechten</li>"
						+ "<li>Ausf&auml;lle oder Probleme im Hama Cluster</li>"
						+ "<li>Fachliche Fragen</li></ul>"
						+ "<p>Sinn und Zweck des Projekts ist es, dass wir zeitaufw&auml;ndige Anfragen dokumentieren, um genauer sagen zu k&ouml;nnen wof&uuml;r wir beim Support viel Zeit aufwenden m&uuml;ssen. F&uuml;r das Bereitschaftsteam soll dieses Projekt auch erste Anlaufstelle sein, um Probleme im Cluster zu dokumentieren bzw. um sehen zu k&ouml;nnen, ob es aktuell Probleme im Cluster gibt und wer daran arbeitet. Oftmals kommen bei Problemen mehrere Meldungen bei unterschiedlichen Stellen bei uns an.</p>"
						+ "<p>&nbsp;</p>" + "<p>&nbsp;</p>");
		entry1.setProperty("jst:creationDate", Calendar.getInstance());

		session.save();
	}

	public ContentViewModel retrieveContent() throws RepositoryException {
		Node root = session.getRootNode();
		Node node = root.getNode("myBlog");
		ContentViewModel contentViewModel = getContentViewModel(node);
		return contentViewModel;
	}

	public ContentViewModel retrieveContent(String relPath) {
		try {
			Node root = session.getRootNode();
			Node node = root.getNode(relPath);
			ContentViewModel contentViewModel = getContentViewModel(node);
			return contentViewModel;
		} catch (PathNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private ContentViewModel getContentViewModel(Node node) throws RepositoryException {
		ContentViewModel contentViewModel = new ContentViewModel();
		contentViewModel.setPath(node.getPath());
		contentViewModel.setName(node.getName());
		contentViewModel.setProperties(new HashMap<>());
		contentViewModel.setChildren(new ArrayList<>());

		PropertyIterator propertyIterator = node.getProperties();
		while (propertyIterator.hasNext()) {
			Property property = propertyIterator.nextProperty();
			ContentPropertyViewModel contentPropertyViewModel = new ContentPropertyViewModel();
			contentPropertyViewModel.setName(property.getName());
			if (property.isMultiple()) {
				contentPropertyViewModel.setValue("skipped multiple values");
			} else {
				contentPropertyViewModel.setValue(property.getString());
			}
			contentViewModel.getProperties().put(property.getName(), contentPropertyViewModel);
		}

		NodeIterator nodeIterator = node.getNodes();
		while (nodeIterator.hasNext()) {
			Node childNode = nodeIterator.nextNode();
			ContentViewModel childContentViewModel = getContentViewModel(childNode);
			childContentViewModel.setParent(contentViewModel);
			contentViewModel.getChildren().add(childContentViewModel);
		}

		return contentViewModel;
	}

	public void removeContent() throws RepositoryException {
		Node root = session.getRootNode();

		// Remove content
		root.getNode("hello").remove();
		session.save();
	}

}
