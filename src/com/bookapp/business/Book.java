package com.bookapp.business;

import java.io.Serializable;

public class Book implements Serializable, Comparable<Book> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String author;
	private int pages;
	private int recommendedAge;
	private Member owner;
	private Member holder;
	private boolean lendable;
	private String errorMsg;

	public Book() {
		title = "";
		author = "";
		pages = 0;
		recommendedAge = 0;
		setOwner(null);
		setHolder(null);
		lendable = false;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPages() {
		if (pages == 0) {
			pages = 10;
		}
		return pages;
	}

	public void setPages(String pages) {
		this.pages = myParseInt(pages, 10);
	}

	public void setPages(int pages) {
		this.pages = mySetInt(pages, 10);
	}

	public int getRecommendedAge() {
		if (recommendedAge == 0) {
			recommendedAge = 10;
		}
		return recommendedAge;
	}

	public void setRecommendedAge(String recommendedAge) {
		this.recommendedAge = myParseInt(recommendedAge, 10);
	}
	
	public void setRecommendedAge(int recommendedAge) {
		this.recommendedAge = mySetInt(recommendedAge, 10);
	}



	public Member getOwner() {
		return owner;
	}

	public void setOwner(Member owner) {
		this.owner = owner;
	}

	public Member getHolder() {
		return holder;
	}

	public void setHolder(Member holder) {
		this.holder = holder;
	}

	public boolean isLendable() {
		return lendable;
	}

	public void setLendable(boolean lendable) {
		this.lendable = lendable;
	}

	private int myParseInt(String num, int def) {
		int output = def;

		try {
			output = Integer.parseInt(num);
		} catch (Exception e) {
			// Do nothing
		}
		return output;
	}

	private int mySetInt(int num, int def) {
		return num > 0 ? num : def;
	}

	public String getErrorMsg() {
		setErrorMsg();
		return errorMsg;
	}

	private void setErrorMsg() {

		StringBuilder sb = new StringBuilder("");

		if (getTitle().trim().length() == 0) {
			sb.append("Title must be at least one character in length. ");
		}

		if (getAuthor().trim().length() == 0) {
			sb.append("Author must be at least one character in length. If unknown, use 'UNK' or 'Unknown'");
		}

		if (owner.getId() == 0 || holder.getId() == 0) {
			sb.append("Could not find login credentials. Try to log in again before adding a book. ");
		}
		errorMsg = sb.toString();
	}

	@Override
	public int compareTo(Book other) {

		if (!this.getTitle().equalsIgnoreCase(other.getTitle())) {
			return this.getTitle().compareToIgnoreCase(other.getTitle());
		}

		if (!this.getAuthor().equalsIgnoreCase(other.getAuthor())) {
			return this.getAuthor().compareToIgnoreCase(other.getAuthor());
		}

		return getOwner().compareTo(other.getOwner());
	}

}
