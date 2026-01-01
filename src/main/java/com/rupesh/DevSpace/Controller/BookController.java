package com.rupesh.DevSpace.Controller;

import com.rupesh.DevSpace.DTO.BookDTO;
import com.rupesh.DevSpace.Entity.Book;
import com.rupesh.DevSpace.Service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Admin")
public class BookController {

    private final BookService bookService;

    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> All()
    {
        return ResponseEntity.ok(bookService.getAllBook());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBook(@RequestParam String isbn) {
       return ResponseEntity.ok(bookService.softDeleteBook(isbn));
    }

    @GetMapping("/getIsbn")
    public ResponseEntity<BookDTO> getByIsbn(@RequestParam String isbn)
    {
        return ResponseEntity.ok(bookService.getByIsbn(isbn));
    }

    @PostMapping("/add")
    public ResponseEntity<String> create(@RequestBody BookDTO bookDTO)
    {
        return ResponseEntity.ok(bookService.createBook(bookDTO));
    }
}
