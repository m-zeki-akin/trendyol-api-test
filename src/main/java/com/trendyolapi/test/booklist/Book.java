package com.trendyolapi.test.booklist;

import com.sun.istack.NotNull;

import java.util.Objects;

import javax.persistence.*;

@Entity
public class Book {

	private @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_generator")
	@SequenceGenerator(name="author_generator", initialValue=0, sequenceName = "author_seq", allocationSize = 1) Long id;
	private @NotNull String author;
	private @NotNull String title;
	
	Book(){}
	
	public Book(String author, String title){// no need builder class
		
		this.author = author;
		this.title = title;

	}
	
	public Long getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public boolean equals(Object o) {

		if (this == o)
		  return true;
		if (!(o instanceof Book))
		  return false;
		Book book = (Book) o;
		return Objects.equals(this.id, book.id) && Objects.equals(this.author, book.author)
			&& Objects.equals(this.title, book.title);
	}
	  
	  @Override
	  public int hashCode() {
	    return Objects.hash(this.id, this.author, this.title);
	  }

	  @Override
	  public String toString() {
	    return String.format("Book(id=%s, author=%s, title=%s)", this.id, this.author, this.title);
	  }
	
}
