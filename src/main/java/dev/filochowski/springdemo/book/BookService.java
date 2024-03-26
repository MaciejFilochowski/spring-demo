package dev.filochowski.springdemo.book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book saveBook(Book book);

    Optional<Book> getBook(Long id);

    List<Book> getBooks();

    boolean deleteBook(Long id);

    Optional<Book> updateBook(Long id, Book book);
}
