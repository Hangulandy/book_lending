package com.bookapp.business;

import java.io.Serializable;
import java.util.regex.Pattern;

public class Member implements Serializable, Comparable<Member> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String email;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private AccountType accountType;
	private String errorMsg;
	private boolean loggedIn;
	public final static int MAX_NAME_LEN = 40;

	public Member() {
		this("", "", "", "", "");
	}

	public Member(String email, String firstName, String lastName, String userName, String password) {
		id = -1;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.accountType = new AccountType();
		this.errorMsg = "";
		setLoggedIn(false);
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String makeName(String string) {

		String tempString = "";

		if (string != null && string.trim().length() > 0) {
			tempString = string;
		}

		return tempString;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = makeName(lastName);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = makeName(firstName);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {

		String tempString = "";

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email != null && pat.matcher(email).matches()) {
			tempString = email;
		}

		this.email = tempString;

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = makeName(userName);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	

	public String getErrorMsg() {
		setErrorMsg();
		return errorMsg;
	}

	private void setErrorMsg() {

		StringBuilder sb = new StringBuilder("");

		if (getEmail().trim().length() == 0) {
			sb.append("Email address is not valid. ");
		}

		if (getFirstName().trim().length() == 0) {
			sb.append("First name must be at least one character in length. ");
		}

		if (getFirstName().trim().length() > MAX_NAME_LEN) {
			sb.append(String.format("First name can only be no more than %d characters long.", MAX_NAME_LEN));
		}

		if (getLastName().trim().length() == 0) {
			sb.append("Last name must be at least one character in length. ");
		}

		if (getLastName().trim().length() > MAX_NAME_LEN) {
			sb.append(String.format("Last name can only be no more than %d characters long.", MAX_NAME_LEN));
		}

		if (getUserName().trim().length() == 0) {
			sb.append("User name must be at least one character in length. ");
		}

		if (getUserName().trim().length() > MAX_NAME_LEN) {
			sb.append(String.format("User name can only be no more than %d characters long.", MAX_NAME_LEN));
		}

		if (getPassword() == null) {
			sb.append("Passwords do not match. ");
		} else {
			if (getPassword().length() < 8 || getPassword().length() > 20) {
				sb.append("Password must be between 8 and 20 characters in length. ");
			}
		}
		errorMsg = sb.toString();
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String toString() {
		return String.format("Member %s %s with username %s", getFirstName(), getLastName(), getUserName());
	}

	@Override
	public int compareTo(Member other) {
		return getId().compareTo(other.getId());
	}
	
	public boolean canLendAndBorrow() {
		return !accountType.isLimited();
	}

}
