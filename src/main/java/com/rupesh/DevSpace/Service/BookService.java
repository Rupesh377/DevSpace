package com.rupesh.DevSpace.Service;

import com.rupesh.DevSpace.DTO.BookDTO;
import com.rupesh.DevSpace.Entity.Book;
import com.rupesh.DevSpace.Repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;
    private final ModelMapper modelMapper;

    public String softDeleteBook(String isbn) {
        Book book = bookRepo.findByIsbn(isbn).orElseThrow(()-> new RuntimeException("Book not exists"));
        book.setDeleted(true);
        bookRepo.save(book);
        return "Book Deleted with isbn " + isbn;
    }

    public List<BookDTO> getAllBook() {

        return bookRepo.findByDeletedFalse().stream().map(
                book -> modelMapper.map(book , BookDTO.class)).toList();
    }


    public BookDTO getByIsbn(String isbn) {
        Book book= bookRepo.findByIsbn(isbn).orElseThrow(()-> new RuntimeException("Book not available"));
        return modelMapper.map(book , BookDTO.class);
    }


    public  String createBook(BookDTO bookDTO) {

        bookRepo.findByIsbn(bookDTO.getIsbn()).ifPresent(book -> {
            throw new RuntimeException("Book with ISBN " + bookDTO.getIsbn() + " already exists");});
            Book book = Book.builder()
                    .title(bookDTO.getTitle())
                    .author(bookDTO.getAuthor())
                    .isbn(bookDTO.getIsbn())
                    .deleted(false)
                    .build();
            bookRepo.save(book);
        return "Added book with isbn no. "+bookDTO.getIsbn();
    }
}
