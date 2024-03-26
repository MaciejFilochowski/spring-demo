package dev.filochowski.springdemo.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class BookRepositoryTest {
    private static final Long ID = 1L;
    private static final Book BOOK = new Book("Ostatnie życzenie", new BigInteger("9788375780635"), 332, Genre.FANTASY, LocalDate.of(2014, Month.SEPTEMBER, 25));
    @Autowired
    private BookRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void saveBookWhenBookExists() {
        Book savedBook = underTest.save(BOOK);

        Optional<Book> foundOptionalBook = underTest.findById(savedBook.getId());

        assertEquals(foundOptionalBook.get(), savedBook);
    }

    @Test
    void findBookByIdWhenBookDoesNotExists() {
        Optional<Book> optionalBook = underTest.findById(ID);

        assertThat(optionalBook.isPresent()).isFalse();
    }
}