package dev.filochowski.springdemo.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final Long ID = 1L;
    private static final Book BOOK = new Book("Ostatnie życzenie", new BigInteger("9788375780635"), 332, Genre.FANTASY, LocalDate.of(2014, Month.SEPTEMBER, 25));
    @Mock
    private BookRepository bookRepository;
    private BookService underTest;
    @Captor
    ArgumentCaptor<Book> bookArgumentCaptor;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        underTest = new BookServiceImpl(bookRepository);
    }

    @Test
    void getAllBooks() {
        underTest.getBooks();
        verify(bookRepository).findAll();
    }

    @Test
    void addBook() {
        underTest.saveBook(BOOK);

        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();
        assertThat(capturedBook).isEqualTo(BOOK);
    }

    @Test
    void deleteBook() {
        given(bookRepository.findById(ID)).willReturn(Optional.of(BOOK));

        underTest.deleteBook(ID);

        verify(bookRepository).delete(BOOK);
    }

    @Test
    void deleteWhenBookDoesNotExits() {
        given(bookRepository.findById(ID)).willReturn(Optional.empty());

        assertThat(underTest.deleteBook(ID)).isFalse();

        verify(bookRepository, never()).save(any());
    }
}