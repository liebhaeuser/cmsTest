package org.jstage.cms;

public class ContentViewModels {
	
	private ContentService contentService;
	
	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	public ContentViewModel getContentViewModel(String relPath) {
		return contentService.retrieveContent(relPath);
	}
	
}
