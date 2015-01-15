package org.jstage.cms;

import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.JcrUtils;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

	private Repository repository;

	private Session session;

	@PostConstruct
	private void init() throws RepositoryException {
		repository = JcrUtils.getRepository();
		session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
	}

	@PreDestroy
	private void cleanUp() {
		session.logout();
	}

	public void createContent() throws RepositoryException {
		Node root = session.getRootNode();

		// Store content
		Node hello = root.addNode("hello");
		Node world = hello.addNode("world");
		world.setProperty("message", "Hello, World!");
		world.setProperty("creationDate", Calendar.getInstance());
		session.save();
	}

	public ContentViewModel retrieveContent() throws RepositoryException {
		Node root = session.getRootNode();
		Node node = root.getNode("hello/world");
		ContentViewModel contentViewModel = getContentViewModel(node);
		return contentViewModel;
	}

	private ContentViewModel getContentViewModel(Node node) throws RepositoryException {
		ContentViewModel contentViewModel = new ContentViewModel();
		contentViewModel.setPath(node.getPath());
		contentViewModel.setProperties(new ArrayList<>());
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
			contentViewModel.getProperties().add(contentPropertyViewModel);
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
