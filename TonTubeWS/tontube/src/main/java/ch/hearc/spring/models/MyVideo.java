package ch.hearc.spring.models;

import org.springframework.web.multipart.MultipartFile;

public class MyVideo {
	
	private String id;
	private String title;
	private String description;
    private String[] tags;
    private String privacy;
    private MultipartFile file;
    
	public MyVideo() {
	}
	
	public MyVideo(String title, String description, String[] tags, String privacy, MultipartFile file) {
		super();
		this.title = title;
		this.description = description;
		this.tags = tags;
		this.privacy = privacy;
		this.file = file;
	}
	
	public MyVideo(String id, String title, String description, String[] tags) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.tags = tags;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public String getPrivacy() {
		return privacy;
	}
	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
    
    
}
