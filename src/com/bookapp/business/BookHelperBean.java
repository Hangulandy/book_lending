package com.bookapp.business;

import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;

import com.bookapp.data.BookDB;

public class BookHelperBean {
	
	
	public BookHelperBean() {
		//default constructor
	}
	
	
	public TreeSet<Book> getMemberBooks(int memberId) {
		return BookDB.getMemberBooks(memberId);
	}

	public TreeSet<Book> getAllBooks() {
		TreeSet<Book> allBooks = BookDB.getAllBooks();
		System.out.println("getAllBooks -- " + ArrayUtils.toString(allBooks));
		return allBooks;
		
//		return BookDB.getAllBooks();
	}
	
}
