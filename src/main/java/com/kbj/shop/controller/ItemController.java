package com.kbj.shop.controller;

import com.kbj.shop.domain.item.BookDto;
import com.kbj.shop.domain.item.Book;
import com.kbj.shop.domain.item.Item;
import com.kbj.shop.domain.item.ItemDto;
import com.kbj.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookDto());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookDto bookDto) {
        Book book = Book.createEntity(bookDto);
        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();

        model.addAttribute("items", items);
        return "items/itemList";
    }


    @GetMapping("/items/{itemId}")
    @ResponseBody
    public ItemDto getItem(@PathVariable("itemId") Long itemId) {
        ItemDto item = itemService.findItem(itemId);
        return item;
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        ItemDto item = itemService.findItem(itemId);

        model.addAttribute("form", item);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, BookDto form) {
        itemService.updateItem(itemId, form);
        return "redirect:/items";
    }
}
