package org.jstage.cms;

import java.util.Collection;

public class ContentViewModel {

	private ContentViewModel parent;

	private Collection<ContentViewModel> children;

	private String path;

	private Collection<ContentPropertyViewModel> properties;

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

	public Collection<ContentPropertyViewModel> getProperties() {
		return properties;
	}

	public void setProperties(Collection<ContentPropertyViewModel> properties) {
		this.properties = properties;
	}

}
