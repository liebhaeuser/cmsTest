package org.jstage.cms;

import java.util.Collection;
import java.util.Map;

public class ContentViewModel {

	private ContentViewModel parent;

	private Collection<ContentViewModel> children;

	private String path;

	private String name;

	private Map<String, ContentPropertyViewModel> properties;

	public ContentViewModel getParent() {
		return parent;
	}

	public void setParent(ContentViewModel parent) {
		this.parent = parent;
	}

	public Collection<ContentViewModel> getChildren() {
		return children;
	}

	public void setChildren(Collection<ContentViewModel> children) {
		this.children = children;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, ContentPropertyViewModel> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, ContentPropertyViewModel> properties) {
		this.properties = properties;
	}

}
