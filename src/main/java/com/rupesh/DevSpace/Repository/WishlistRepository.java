package com.rupesh.DevSpace.Repository;

import com.rupesh.DevSpace.Entity.Book;
import com.rupesh.DevSpace.Entity.Wishlist;
import com.rupesh.DevSpace.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist , Long> {

    boolean existsByUserAndBook(User user, Book book);

    List<Wishlist> findByUser(User user);

    Optional<Wishlist> findByUserAndBook(User user , Book book);

    @Modifying
    @Transactional
    void deleteByUserAndBook(User user, Book book);

    Optional<Wishlist> findByUserAndBook_Title(User user, String title);

    @Modifying
    void delete(Wishlist wishlist);
}
