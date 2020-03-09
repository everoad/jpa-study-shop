package com.kbj.shop.domain.item;

import lombok.*;

@Getter @Setter
public class BookDto extends ItemDto {

    private String author;
    private String isbn;

}
