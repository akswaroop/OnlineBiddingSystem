package com.akshay.OnlineBiddingSystem.Controller;

import com.akshay.OnlineBiddingSystem.Entity.AuctionItem;
import com.akshay.OnlineBiddingSystem.Entity.Bid;
import com.akshay.OnlineBiddingSystem.Entity.User;
import com.akshay.OnlineBiddingSystem.Repository.AuctionItemRepository;
import com.akshay.OnlineBiddingSystem.Repository.BidRepository;
import com.akshay.OnlineBiddingSystem.Repository.UserRepository;
import com.akshay.OnlineBiddingSystem.Service.AuctionItemService;
import com.akshay.OnlineBiddingSystem.Service.BidService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/items")
public class AuctionItemController {

    private final AuctionItemService auctionItemService;
    private final BidService bidService;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;


    public AuctionItemController(AuctionItemService auctionItemService, BidService bidService, UserRepository userRepository, BidRepository bidRepository) {
        this.auctionItemService = auctionItemService;
        this.bidService = bidService;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
    }

    @GetMapping()
    public  String showMarketPlace(Model model,
                                   @org.springframework.security.core.annotation.AuthenticationPrincipal
                                   org.springframework.security.core.userdetails.UserDetails loggedInUser) {
        List<AuctionItem> auctionItems = auctionItemService.getAllItems();
        Map<Long,Double> livePrices = new HashMap<>();
        for(AuctionItem item:auctionItems){
            Double amount = auctionItemService.getCurrentPrice(item.getId());
            livePrices.put(item.getId(), amount);
        }
        model.addAttribute("auctionItems",auctionItems);
        model.addAttribute("livePrices",livePrices);
        if(loggedInUser!=null){
            model.addAttribute("currentUsername",loggedInUser.getUsername());
            boolean isAdmin = loggedInUser.getAuthorities().stream().anyMatch(auth-> auth.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin",isAdmin);
        }else
        {
            model.addAttribute("currentUsername",null);
            model.addAttribute("isAdmin",null);
        }
        return "marketplace";
    }
    @PostMapping("create")
    public String create(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam Double startingPrice,
            @RequestParam String expirationDateStr,
            Principal principal){
        String userName = principal.getName();
        User user = userRepository.findByUserName(userName).orElseThrow(()->new UsernameNotFoundException("user Name not found: "+ userName));
        Long sellerId = user.getId();
        try{
            LocalDateTime expirationDate = LocalDateTime.parse(expirationDateStr);
            auctionItemService.listItem(sellerId ,title,description,startingPrice,expirationDate);
            return"redirect:/items";
        }
        catch (Exception e)
        {
            return "errorPage";
        }
    }
    @GetMapping("/details")
    public String showItemDetails(@RequestParam Long id, Model model) {
        AuctionItem item = auctionItemService.getItemById(id);
        Double currentPrice = auctionItemService.getCurrentPrice(id);
        List<Bid> bidHistory = bidRepository.findByAuctionItemIdOrderByBidTimeDesc(id);
        model.addAttribute("item", item);
        model.addAttribute("currentPrice", currentPrice);
        model.addAttribute("bidHistory",bidHistory);// <-- Stuff it in the backpack!
        return "itemDetails";
    }
    @GetMapping("/new")
    public String showCreateForm(){
        return "createItem";
    }

}
