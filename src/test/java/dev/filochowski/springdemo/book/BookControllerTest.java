package dev.filochowski.springdemo.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.filochowski.springdemo.config.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(JwtService.class)
@WebMvcTest(BookController.class)
@WithMockUser(roles = "USER")
class BookControllerTest {
    private static final String PATH = "/api/v1/books";
    private static final String PATH_ID = PATH + "/{id}";
    private static final Long ID = 1L;
    private static final Book BOOK = new Book("Ostatnie życzenie", new BigInteger("9788375780635"), 332, Genre.FANTASY, LocalDate.of(2014, Month.SEPTEMBER, 25));
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    BookService bookService;

    @Test
    void getBooks() throws Exception {
        mockMvc.perform(get(PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getBookWhenBookExits() throws Exception {
        given(bookService.getBook(ID)).willReturn(Optional.of(new Book()));

        mockMvc.perform(get(PATH_ID, ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getBookWhenBookDoesNotExist() throws Exception {
        given(bookService.getBook(ID)).willReturn(Optional.empty());

        mockMvc.perform(get(PATH_ID, ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveBook() throws Exception {
        given(bookService.saveBook(any(Book.class))).willReturn(BOOK);

        mockMvc.perform(post(PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BOOK))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void deleteBookWhenBookExits() throws Exception {
        given(bookService.deleteBook(ID)).willReturn(true);

        mockMvc.perform(delete(PATH_ID, ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(ID);
    }

    @Test
    void deleteBookWhenBookDoesNotExist() throws Exception {
        given(bookService.deleteBook(ID)).willReturn(false);

        mockMvc.perform(delete(PATH_ID, ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(bookService).deleteBook(ID);
    }

    @Test
    void updatePostBookWhenBookExits() throws Exception {
        given(bookService.updateBook(ID, BOOK)).willReturn(Optional.of(new Book()));

        mockMvc.perform(put(PATH_ID, ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BOOK))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(bookService).updateBook(ID, BOOK);
    }

    @Test
    void updatePostBookWhenBookDoesNotExit() throws Exception {
        given(bookService.updateBook(ID, BOOK)).willReturn(Optional.empty());
        given(bookService.saveBook(BOOK)).willReturn(BOOK);

        mockMvc.perform(put(PATH_ID, ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BOOK))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        verify(bookService).saveBook(BOOK);
    }

    @Test
    void updatePatchBookWhenBookExits() throws Exception {
        given(bookService.updateBook(ID, BOOK)).willReturn(Optional.of(new Book()));

        mockMvc.perform(patch(PATH_ID, ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BOOK))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(bookService).updateBook(ID, BOOK);
    }

    @Test
    void updatePatchBookWhenBookDoesNotExit() throws Exception {
        given(bookService.updateBook(ID, BOOK)).willReturn(Optional.empty());

        mockMvc.perform(patch(PATH_ID, ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BOOK))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(bookService).updateBook(ID, BOOK);
    }
}