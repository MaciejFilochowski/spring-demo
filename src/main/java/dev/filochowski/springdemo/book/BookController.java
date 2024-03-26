package dev.filochowski.springdemo.book;

import dev.filochowski.springdemo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<CollectionModel<Book>> getBooks() {
        List<Book> books = bookService.getBooks();

        for (Book book : books) {
            Link selfLink = linkTo(BookController.class).slash(book.getId()).withSelfRel();
            book.add(selfLink);
        }

        Link selfLink = linkTo(BookController.class).withSelfRel();
        CollectionModel<Book> bookCollectionModel = CollectionModel.of(books, selfLink);

        return ResponseEntity.ok().body(bookCollectionModel);
    }

    @GetMapping("{bookId}")
    public ResponseEntity<Book> getBook(@PathVariable("bookId") Long id) {
        Optional<Book> optionalBook = bookService.getBook(id);

        if (optionalBook.isEmpty()) {
            throw new NotFoundException();
        }

        Book book = optionalBook.get();
        Link selfLink = linkTo(BookController.class).slash(book.getId()).withSelfRel();
        book.add(selfLink);

        return ResponseEntity.ok().body(book);
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody Book book) {
        Book createdBook = bookService.saveBook(book);
        Link selfLink = linkTo(BookController.class).slash(book.getId()).withSelfRel();
        book.add(selfLink);

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
            Link selfLink = linkTo(BookController.class).slash(createdBook.getId()).withSelfRel();
            createdBook.add(selfLink);

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/books/" + createdBook.getId()).toUriString());
            return ResponseEntity.created(uri).body(createdBook);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{bookId}")
    public ResponseEntity<HttpStatus> partialUpdateBook(@PathVariable("bookId") Long id, @RequestBody Book request) {
        Optional<Book> optionalBook = bookService.updateBook(id, request);

        if (optionalBook.isEmpty()){
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}