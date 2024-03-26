package dev.filochowski.springdemo.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public boolean deleteBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Book> updateBook(Long id, Book book) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Book updatedBook = optionalBook.get();
            if (book.getTitle() != null) {
                updatedBook.setTitle(book.getTitle());
            }
            if (book.getIsbn() != null) {
                updatedBook.setIsbn(book.getIsbn());
            }
            if (book.getPages() != null) {
                updatedBook.setPages(book.getPages());
            }
            if (book.getGenre() != null) {
                updatedBook.setGenre(book.getGenre());
            }
            if (book.getPublicationDate() != null) {
                updatedBook.setPublicationDate(book.getPublicationDate());
            }

            return Optional.of(bookRepository.save(updatedBook));
        } else {
            return Optional.empty();
        }
    }
}
