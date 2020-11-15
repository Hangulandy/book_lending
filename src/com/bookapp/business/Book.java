package com.bookapp.business;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Book implements Comparable<Book> {

	private int id;
	private String title;
	private String author;
	private int pages;
	private int recommendedAge;
	private int ownerId;
	private int holderId;
	private boolean lendable;
	private String errorMsg;

	public Book() {
		title = "";
		author = "";
		pages = 0;
		recommendedAge = 0;
		ownerId = 0;
		holderId = 0;
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

	//FIXME: IS there a reason that this comes in as a String?
	public void setPages(String pages) {
		this.pages = myParseInt(pages, 10);
	}

	//FIXME: This is producing strange output?
	public int getRecommendedAge() {
		if (recommendedAge == 0) {
			recommendedAge = 10;
		}
		return recommendedAge;
	}

	public void setRecommendedAge(String recommendedAge) {
		this.recommendedAge = myParseInt(recommendedAge, 10);
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getHolderId() {
		return holderId;
	}

	public void setHolderId(int holderId) {
		this.holderId = holderId;
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
		
		if (ownerId == 0 || holderId == 0) {
			sb.append("Could not find login credentials. Try to log in again before adding a book. ");
		}
		errorMsg = sb.toString();
	}
	
	//This is mostly for testing and debugging
	@Override
	public String toString() {
		String delim =",";
		return (
				getId() + delim + 
			    getTitle() + delim + 
				getAuthor() + delim + 
			    getPages() + delim + 
				getRecommendedAge() + delim +
			    getOwnerId() + delim + 
				getHolderId() + delim + 
			    isLendable()
				);
	}
	
	@Override
	public int compareTo(Book otherBook) {
		return new CompareToBuilder()
				.append(this.title, otherBook.getTitle())
				.append(this.author, otherBook.getAuthor())
				.append(this.ownerId, otherBook.getOwnerId())
				.toComparison();
	}

	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Book otherBook = (Book) o;
		
		return new EqualsBuilder()
			.append(this.title, otherBook.getTitle())
			.append(this.author, otherBook.getAuthor())
			.append(this.ownerId, otherBook.getOwnerId())
			.isEquals();
	}

}
