package com.rupesh.DevSpace.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistRequestDTO {

    private String title;

    private String description;
}
