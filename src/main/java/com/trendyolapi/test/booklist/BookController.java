package com.trendyolapi.test.booklist;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class BookController {
	
	private static final Logger log = LoggerFactory.getLogger(BookController.class);

	@Autowired
	ExceptionHandlerConfig exceptionHandlerConfig;

	private final BookRepository repository;

	BookController(BookRepository repository){
		this.repository = repository;
	}
	
	//  Books "/api/books"
	
	@GetMapping("/api/books")
	List<Book> getAll(){
		return repository.findAll();
	}
	
	@PostMapping("/api/books")
	Book add(@RequestBody Book newBook) {

		String newAuthor = newBook.getAuthor();
		String newTitle = newBook.getTitle();

		if(newAuthor==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'author' is required");
		}else if(newTitle==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'title' is required");
		}else if(newAuthor.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'author' cannot be empty");
		}else if(newTitle.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'title' cannot be empty");
		}
		
		
		for(Book book : repository.findAll()) {
			if(newAuthor.equalsIgnoreCase(book.getAuthor())
			 && newTitle.equalsIgnoreCase(book.getTitle())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Another book with similar title and author already exists");
			}
		}
		
		return repository.save(newBook);
	}

	@PutMapping("/api/books")
	Book updateBook(@RequestBody Book newBook, @PathVariable Long id) {

		String newAuthor = newBook.getAuthor();
		String newTitle = newBook.getTitle();

		if(newAuthor==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'author' is required");
		}else if(newTitle==null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'title' is required");
		}else if(newAuthor.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'author' cannot be empty");
		}else if(newTitle.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field 'title' cannot be empty");
		}

		for(Book book : repository.findAll()) {
			if(newAuthor.equalsIgnoreCase(book.getAuthor())
					&& newTitle.equalsIgnoreCase(book.getTitle())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Another book with similar title and author already exists");
			}
		}

		return repository.findById(id)
				.map(book ->{
					book.setAuthor(newAuthor);
					book.setTitle(newTitle);
					return repository.save(book);
				})
				.orElseGet(() -> repository.save(newBook));
	}
	
	@DeleteMapping("/api/books")
	void deleteAll() {
		repository.deleteAll();
	}
	
	//Book "/api/books/{id}"
	
	@GetMapping("/api/books/{id}")
	Book get(@PathVariable Long id) {
		return repository.findById(id).orElseThrow(BookNotFoundException::new);
	}
	
	@DeleteMapping("/api/books/{id}")
	void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
}
