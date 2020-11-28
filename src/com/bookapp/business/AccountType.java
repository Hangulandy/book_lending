package com.bookapp.business;

import java.io.Serializable;

public class AccountType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	
	public AccountType() {
		id = 3;
		title = "limited";
	}
	
	public AccountType(int type) {
		id = type;
		switch(id) {
		case 1:
			title = "member";
			break;
		case 2:
			title = "admin";
			break;
		case 3:
			title = "limited";
			break;
		default:
			title = "limited";
			id = 3;			
		}
	}
	
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
	
	public boolean isLimited() {
		return title.equalsIgnoreCase("limited");
	}

}
