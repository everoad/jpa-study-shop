package com.kbj.shop.service;

import com.kbj.shop.common.ItemType;
import com.kbj.shop.domain.item.BookDto;
import com.kbj.shop.domain.item.Item;
import com.kbj.shop.domain.item.ItemDto;
import com.kbj.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;


    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }


    @Transactional
    public void updateItem(Long itemId, BookDto form) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "No item data by itemId="+itemId);
        }
        Item item = optionalItem.get();

        item.change(form);
    }


    public List<Item> findItems() {
        return itemRepository.findAll();
    }


    public ItemDto findItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No item data by itemId="+itemId);
        }
        Item item = optionalItem.get();

        ItemType itemType = ItemType.findItemType(item);
        ItemDto itemDto = modelMapper.map(item, itemType.getDtoClass());

        return itemDto;
    }


}
