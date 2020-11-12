package com.bookapp.business;

import java.sql.Date;

public class Request {
	
	// TODO - This class has no functionality yet, i.e. no business rules.

	private int id;
	private int ownerId;
	private int requesterId;
	private Date dateRequested;
	private boolean approved;
	private Date dateApprovedSelected;
	private Date dateSentByOwner;
	private Date dateReceivedByRequester;
	private Date dateSentByRequester;
	private Date dateReceivedByOwner;
	private String remarks;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(int requesterId) {
		this.requesterId = requesterId;
	}

	public Date getDateRequested() {
		return dateRequested;
	}

	public void setDateRequested(Date dateRequested) {
		this.dateRequested = dateRequested;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public Date getDateApprovedSelected() {
		return dateApprovedSelected;
	}

	public void setDateApprovedSelected(Date dateApprovedSelected) {
		this.dateApprovedSelected = dateApprovedSelected;
	}

	public Date getDateSentByOwner() {
		return dateSentByOwner;
	}

	public void setDateSentByOwner(Date dateSentByOwner) {
		this.dateSentByOwner = dateSentByOwner;
	}

	public Date getDateReceivedByRequester() {
		return dateReceivedByRequester;
	}

	public void setDateReceivedByRequester(Date dateReceivedByRequester) {
		this.dateReceivedByRequester = dateReceivedByRequester;
	}

	public Date getDateSentByRequester() {
		return dateSentByRequester;
	}

	public void setDateSentByRequester(Date dateSentByRequester) {
		this.dateSentByRequester = dateSentByRequester;
	}

	public Date getDateReceivedByOwner() {
		return dateReceivedByOwner;
	}

	public void setDateReceivedByOwner(Date dateReceivedByOwner) {
		this.dateReceivedByOwner = dateReceivedByOwner;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	// TODO - somehow there need to be remarks that can be entered. Should this be
	// another table?

}
