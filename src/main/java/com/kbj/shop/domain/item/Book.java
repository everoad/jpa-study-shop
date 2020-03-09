package com.kbj.shop.domain.item;

import com.kbj.shop.common.ItemType;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {

    private String author;
    private String isbn;


    public static Book createEntity(BookDto dto) {
        Book entity = new Book();
        return entity;
    }

    public void change(BookDto form) {
        super.change(form);
        this.author = form.getAuthor();
        this.isbn = form.getIsbn();
    }
}
