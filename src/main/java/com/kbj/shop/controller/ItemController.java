package com.kbj.shop.controller;

import com.kbj.shop.domain.item.Book;
import com.kbj.shop.domain.item.Item;
import com.kbj.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ModelMapper modelMapper;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm bookForm) {
        Book book = Book.builder()
                .name(bookForm.getName())
                .price(bookForm.getPrice())
                .stockQuantity(bookForm.getStockQuantity())
                .author(bookForm.getAuthor())
                .isbn(bookForm.getIsbn())
                .build();

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items" ,items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findItem(itemId);
        BookForm form = new BookForm();
        form.setAuthor(item.getAuthor());
        form.setId(item.getId());
        form.setName(item.getName());
        form.setIsbn(item.getIsbn());
        form.setStockQuantity(item.getStockQuantity());
        form.setPrice(item.getPrice());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, BookForm form) {
        itemService.updateItem(itemId, form);
        return "redirect:/items";
    }
}
