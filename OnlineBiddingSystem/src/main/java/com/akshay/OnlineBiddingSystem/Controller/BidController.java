package com.akshay.OnlineBiddingSystem.Controller;

import com.akshay.OnlineBiddingSystem.Entity.AuctionItem;
import com.akshay.OnlineBiddingSystem.Entity.User;
import com.akshay.OnlineBiddingSystem.Repository.UserRepository;
import com.akshay.OnlineBiddingSystem.Service.AuctionItemService;
import com.akshay.OnlineBiddingSystem.Service.BidService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;
    private final AuctionItemService auctionItemService;
    private final UserRepository userRepository;

    public BidController(BidService bidService, AuctionItemService auctionItemService, UserRepository userRepository) {
        this.bidService = bidService;
        this.auctionItemService = auctionItemService;
        this.userRepository = userRepository;
    }
    @PostMapping("/place")
    public String placeBid(
            @RequestParam Long itemId,
            Principal principal,
            @RequestParam Double amount,
            Model model) {

        try {
            User user = userRepository.findByUserName(principal.getName()).orElseThrow(()-> new RuntimeException("user not found"));
            bidService.placeBid(itemId, user.getId(), amount);
            return "redirect:/items/details?id=" + itemId;

        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/place")
    public String showBidForm(@RequestParam Long itemId, Model model){
        AuctionItem item = auctionItemService.getItemById(itemId);
        model.addAttribute("item",item);
        return "bidForm";
    }



}
