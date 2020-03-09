package com.kbj.shop.common;

import com.kbj.shop.domain.item.Book;
import com.kbj.shop.domain.item.BookDto;
import com.kbj.shop.domain.item.Item;
import com.kbj.shop.domain.item.ItemDto;
import lombok.Getter;

import javax.persistence.NoResultException;
import java.util.Arrays;

@Getter
public enum ItemType {

    BOOK("B", BookDto.class, Book.class),
    EMPTY("EMPTY", null, null);

    private String dType;
    private Class<? extends ItemDto> dtoClass;
    private Class<? extends Item> entityClass;

    ItemType(String dType, Class<? extends ItemDto> dtoClass, Class<? extends Item> entityClass) {
        this.dType = dType;
        this.dtoClass = dtoClass;
        this.entityClass = entityClass;
    }

    public static ItemType findItemType(Item item) {
        return Arrays.stream(ItemType.values())
                .filter(e -> e.dType.equals(item.getDiscriminatorValue()))
                .findAny()
                .orElseThrow(() -> new NoResultException("No item type by dType=" + item.getDiscriminatorValue()));
    }

}
