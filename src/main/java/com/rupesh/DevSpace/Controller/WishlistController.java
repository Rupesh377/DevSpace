package com.rupesh.DevSpace.Controller;

import com.rupesh.DevSpace.DTO.AddToWishlistDTO;
import com.rupesh.DevSpace.DTO.BookDTO;
import com.rupesh.DevSpace.DTO.WishlistDTO;
import com.rupesh.DevSpace.DTO.WishlistRequestDTO;
import com.rupesh.DevSpace.Entity.Wishlist;
import com.rupesh.DevSpace.Service.BookService;
import com.rupesh.DevSpace.Service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final BookService bookService;

    public WishlistController(WishlistService wishlistService, BookService bookService) {
        this.wishlistService = wishlistService;
        this.bookService = bookService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@AuthenticationPrincipal UserDetails user, @RequestBody AddToWishlistDTO addToWishlistDTO)
    {
        return ResponseEntity.ok(wishlistService.addBookToWishlist(user.getUsername(), addToWishlistDTO));
    }

    @DeleteMapping("/isbn")
    public ResponseEntity<String> removeFromWishlist(@AuthenticationPrincipal UserDetails user,@RequestParam String isbn)
    {
        return ResponseEntity.ok(wishlistService.removeFromWishlist(user.getUsername(), isbn));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookDTO>> getAll(@AuthenticationPrincipal UserDetails user)
    {
        return ResponseEntity.ok(wishlistService.getAllWishlist(user.getUsername()));
    }

    @DeleteMapping("/name")
    public ResponseEntity<String> removeByName(@AuthenticationPrincipal UserDetails user,@RequestParam String title)
    {
        return ResponseEntity.ok(wishlistService.removeFromWishlistByTitle(user.getUsername(), title));
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDTO>> All()
    {
        return ResponseEntity.ok(bookService.getAllBook());
    }
}
