package com.bookapp.business;

import java.util.TreeSet;

import com.bookapp.data.MemberDB;

public class MemberHelperBean {

	public MemberHelperBean() {
		//default constructor
	}

	public TreeSet<Member> getAllMembers() {
		TreeSet<Member> allMembers = MemberDB.getAllMembers();
		return allMembers;
	}
}
