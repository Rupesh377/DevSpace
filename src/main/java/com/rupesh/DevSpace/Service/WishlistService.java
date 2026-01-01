package com.rupesh.DevSpace.Service;

import com.rupesh.DevSpace.DTO.AddToWishlistDTO;
import com.rupesh.DevSpace.DTO.BookDTO;
import com.rupesh.DevSpace.Entity.Book;
import com.rupesh.DevSpace.Entity.User;
import com.rupesh.DevSpace.Entity.Wishlist;
import com.rupesh.DevSpace.Repository.AuthRepository;
import com.rupesh.DevSpace.Repository.BookRepository;
import com.rupesh.DevSpace.Repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepo;
    private final BookRepository bookRepo;
    private final AuthRepository userRepo;
    private final ModelMapper modelMapper;

    public WishlistService(WishlistRepository wishlistRepo, BookRepository bookRepo, AuthRepository userRepo, ModelMapper modelMapper) {
        this.wishlistRepo = wishlistRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public String addBookToWishlist(String email, AddToWishlistDTO addToWishlistDTO) {

            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Book book = bookRepo.findByIsbnAndDeletedFalse(addToWishlistDTO.getIsbn())
                    .orElseThrow(() ->
                            new IllegalStateException("Book with ISBN " + addToWishlistDTO.getIsbn() + " does not exist"));

            if (wishlistRepo.existsByUserAndBook(user, book)) {
                throw new IllegalStateException("Book already in wishlist");
            }

            wishlistRepo.save(Wishlist.builder()
                            .user(user)
                            .book(book)
                            .build());
            return "Added to wishlist";
        }

    @Transactional
    public String removeFromWishlist(String email, String isbn) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepo.findByIsbnAndDeletedFalse(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Wishlist wishlist = wishlistRepo.findByUserAndBook(user , book)
                .orElseThrow(()-> new RuntimeException("Book not is you wishlist"));
        wishlistRepo.deleteByUserAndBook(user, book);
        return "Book with ISBN " + isbn + " removed from wishlist";
    }


    public List<BookDTO> getAllWishlist(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not available"));

        return wishlistRepo.findByUser(user).stream()
                .map(wishlist -> modelMapper.map(wishlist.getBook(), BookDTO.class))
                .toList();
    }


    @Transactional
    public String removeFromWishlistByTitle(String email, String title) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wishlist wishlist = wishlistRepo.findByUserAndBook_Title(user, title)
                .orElseThrow(() ->
                        new RuntimeException("Book not found in user's wishlist")
                );

        wishlistRepo.delete(wishlist);

        return "Book with title '" + title + "' removed from wishlist";
    }

    public List<BookDTO> getAllBook() {

        return bookRepo.findByDeletedFalse().stream().map(
                book -> modelMapper.map(book , BookDTO.class)).toList();
    }
}
