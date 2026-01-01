package com.rupesh.DevSpace.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToWishlistDTO {

    private String title;

    private String author;

    private String isbn;
}
