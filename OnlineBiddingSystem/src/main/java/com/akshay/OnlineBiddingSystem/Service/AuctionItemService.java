package com.akshay.OnlineBiddingSystem.Service;

import com.akshay.OnlineBiddingSystem.Entity.AuctionItem;
import com.akshay.OnlineBiddingSystem.Entity.Bid;
import com.akshay.OnlineBiddingSystem.Entity.User;
import com.akshay.OnlineBiddingSystem.Repository.AuctionItemRepository;
import com.akshay.OnlineBiddingSystem.Repository.BidRepository;
import com.akshay.OnlineBiddingSystem.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionItemService {

    private final UserRepository userRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final BidRepository bidRepository;

    public AuctionItemService(UserRepository userRepository, AuctionItemRepository auctionItemRepository, BidRepository bidRepository) {
        this.userRepository = userRepository;
        this.auctionItemRepository = auctionItemRepository;
        this.bidRepository = bidRepository;
    }

    public void listItem( Long sellerId,String title , String description , Double startingPrice, LocalDateTime expirationDate){
        User seller = userRepository.findById(sellerId)
                .orElseThrow(()-> new IllegalArgumentException("User not found with this id: "+ sellerId));
        if(startingPrice<=0)
        {
            throw new IllegalArgumentException("starting price must be greater than zero");
        }
        if (expirationDate.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("expiration date cannot be current time");
        }
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setDescription(description);
        auctionItem.setTitle(title);
        auctionItem.setSeller(seller);
        auctionItem.setStartingPrice(startingPrice);
        auctionItem.setExpirationDate(expirationDate);
        auctionItemRepository.save(auctionItem);
    }

    public List<AuctionItem> getAllItems(){
        return auctionItemRepository.findAll();
    }
    public AuctionItem getItemById(Long id){
        return auctionItemRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Auction item not found this id:"+ id));
    }
    public Double getCurrentPrice(Long itemId) {
        AuctionItem item = getItemById(itemId);

        return bidRepository.findTopByAuctionItemIdOrderByBidAmountDesc(itemId)
                .map(Bid::getBidAmount)
                .orElse(item.getStartingPrice());
    }
}
