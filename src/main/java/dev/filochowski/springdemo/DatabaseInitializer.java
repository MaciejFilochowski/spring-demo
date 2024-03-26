package dev.filochowski.springdemo;

import dev.filochowski.springdemo.author.Author;
import dev.filochowski.springdemo.author.AuthorRepository;
import dev.filochowski.springdemo.book.Book;
import dev.filochowski.springdemo.book.BookRepository;
import dev.filochowski.springdemo.book.Genre;
import dev.filochowski.springdemo.user.User;
import dev.filochowski.springdemo.user.UserRepository;
import dev.filochowski.springdemo.user.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DatabaseInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<User> users = new ArrayList<>();
        users.add(new User("Maciej", "Filochowski", "contact@filochowski.dev", passwordEncoder.encode("password"), Role.ADMIN, true));

        userRepository.saveAll(users);

        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Andrzej", "Sapkowski", LocalDate.of(1948, Month.JULY, 21)));
        authors.add(new Author("Joanne Kathleen", "Rowling", LocalDate.of(1965, Month.AUGUST, 31)));
        authors.add(new Author("Antoine", "Saint-Exupéry", LocalDate.of(1900, Month.JULY, 29)));
        authors.add(new Author("George", "Orwell", LocalDate.of(1903, Month.JULY, 25)));

        authorRepository.saveAll(authors);

        List<Book> books = new ArrayList<>();
        books.add(new Book("Ostatnie życzenie", new BigInteger("9788375780635"), 332, Genre.FANTASY, LocalDate.of(2014, Month.SEPTEMBER, 25)));
        books.add(new Book("Harry Potter i Kamień Filozoficzny", new BigInteger("9788382656824"), 320, Genre.YOUNG_ADULT_NOVEL, LocalDate.of(2024, Month.JANUARY, 31)));
        books.add(new Book("Mały Książę", new BigInteger("9788366482968"), 80, Genre.FICTION, LocalDate.of(2020, Month.NOVEMBER, 12)));
        books.add(new Book("Rok 1984", new BigInteger("9788381884334"), 352, Genre.CLASSIC, LocalDate.of(2022, Month.APRIL, 5)));

        books.get(0).getAuthors().add(authors.get(0));
        books.get(1).getAuthors().add(authors.get(1));
        books.get(2).getAuthors().add(authors.get(2));
        books.get(3).getAuthors().add(authors.get(3));

        bookRepository.saveAll(books);
    }
}