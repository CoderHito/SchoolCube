package com.hito.schoolcube.entity;

public class News {
	private int id;
	private String title;
	private String content;
	private int openId;
	private int boardId;
	private String userName;
	private String boardName;
	private String boardImgUrl;
	private String localBoardImgUrl;
	private String createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getOpenId() {
		return openId;
	}

	public void setOpenId(int openId) {
		this.openId = openId;
	}

	public int getBoardId() {
		return boardId;
	}

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public String getBoardImgUrl() {
		return boardImgUrl;
	}

	public void setBoardImgUrl(String boardImgUrl) {
		this.boardImgUrl = boardImgUrl;
	}

	public String getLocalBoardImgUrl() {
		return localBoardImgUrl;
	}

	public void setLocalBoardImgUrl(String localBoardImgUrl) {
		this.localBoardImgUrl = localBoardImgUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}