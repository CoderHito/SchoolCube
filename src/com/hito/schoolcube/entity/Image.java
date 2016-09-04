package com.hito.schoolcube.entity;

public class Image {
	private int postNewsId;
	private String path;

	public Image() {
	}

	public Image(int postNewsId, String path) {
		super();
		this.postNewsId = postNewsId;
		this.path = path;
	}

	public int getPostNewsId() {
		return postNewsId;
	}

	public void setPostNewsId(int postNewsId) {
		this.postNewsId = postNewsId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
