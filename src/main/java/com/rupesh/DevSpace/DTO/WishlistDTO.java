package com.rupesh.DevSpace.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDTO {

    private String title;
    private String description;
    private boolean purchased;
}
