package dev.filochowski.springdemo.book;

import dev.filochowski.springdemo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok().body(bookService.getBooks());
    }

    @GetMapping("{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable("bookId") Long id) {
        Optional<Book> optionalBook = bookService.getBook(id);

        if (optionalBook.isEmpty()) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok().body(optionalBook.get());
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        Book createdBook = bookService.saveBook(book);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/books/" + createdBook.getId()).toUriString());
        return ResponseEntity.created(uri).body(createdBook);
    }

    @DeleteMapping("{bookId}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("bookId") Long id) {
        boolean isPresent = bookService.deleteBook(id);

        if (!isPresent) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{bookId}")
    public ResponseEntity<Book> createOrUpdateBook(@PathVariable("bookId") Long id, @RequestBody Book request) {
        Optional<Book> optionalBook = bookService.updateBook(id, request);

        if (optionalBook.isEmpty()) {
            Book createdBook = bookService.saveBook(request);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/books/" + createdBook.getId()).toUriString());
            return ResponseEntity.created(uri).body(createdBook);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{bookId}")
    public ResponseEntity<Book> partialUpdateBook(@PathVariable("bookId") Long id, @RequestBody Book request) {
        Optional<Book> optionalBook = bookService.updateBook(id, request);

        if (optionalBook.isEmpty()){
            throw new NotFoundException();
        }

        return new ResponseEntity<>(optionalBook.get(), HttpStatus.NO_CONTENT);
    }
}