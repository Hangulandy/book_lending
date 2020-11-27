package com.bookapp.business;

import java.io.Serializable;
import java.sql.Date;

public class Request implements Serializable, Comparable<Request> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Book book;
	private Member requester;
	private Date dateRequested;
	private Date dateAnswered;
	private Date dateReceivedByRequester;
	private Date dateClosedByOwner;
	private String remarks;
	private Integer stage; 
	
	// Stages:
	// Stage 1 = request submitted, pending approval;
	// Stage 2 = request approved
	// Stage 3 = Book lent to requester
	// Stage 4 = closed out (either denied or book received by owner)

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Member getRequester() {
		return requester;
	}

	public void setRequester(Member requester) {
		this.requester = requester;
	}

	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}

	



	public Date getDateAnswered() {
		return dateAnswered;
	}

	public void setDateAnswered(Date dateAnswered) {
		this.dateAnswered = dateAnswered;
	}

	public Date getDateReceivedByRequester() {
		return dateReceivedByRequester;
	}

	public void setDateReceivedByRequester(Date dateReceivedByRequester) {
		this.dateReceivedByRequester = dateReceivedByRequester;
	}



	public Date getDateClosedByOwner() {
		return dateClosedByOwner;
	}

	public void setDateClosedByOwner(Date dateClosedByOwner) {
		this.dateClosedByOwner = dateClosedByOwner;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getStage() {
		
		if (getDateClosedByOwner() != null) {
			setStage(4);
			return stage;
		}
		
		if (getDateReceivedByRequester() != null) {
			setStage(3);
			return stage;
		}
		
		if (getDateAnswered() != null) {
			setStage(2);
			return stage;
		}
		
		setStage(1);
		return stage;
	}

	public void setStage(int stage) {
		this.stage = new Integer(stage);
	}

	@Override
	public int compareTo(Request other) {
			
		if (!getStage().equals(other.getStage())) {
			return getStage().compareTo(other.getStage());
		}
		
		if (!getDateRequested().equals(other.getDateRequested())) {
			return getDateRequested().compareTo(other.dateRequested);
		}
		
		if (!getBook().getTitle().equals(other.getBook().getTitle())) {
			return getBook().getTitle().compareTo(other.getBook().getTitle());
		}
		
		return getId().compareTo(other.getId());
	}

	public boolean isOpen() {
		return stage != 4;
	}

}
