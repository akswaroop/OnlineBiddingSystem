package com.akshay.OnlineBiddingSystem.Controller;

import com.akshay.OnlineBiddingSystem.Repository.AuctionItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AuctionItemRepository itemRepository;

    public AdminController(AuctionItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("allItems", itemRepository.findAll());
        return "admin-dashboard";
    }

    @PostMapping("/items/delete")
    public String deleteItem(@RequestParam Long id) {
        itemRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }

}
