package com.akshay.OnlineBiddingSystem.Service;

import com.akshay.OnlineBiddingSystem.Entity.AuctionItem;
import com.akshay.OnlineBiddingSystem.Entity.Bid;
import com.akshay.OnlineBiddingSystem.Entity.User;
import com.akshay.OnlineBiddingSystem.Repository.AuctionItemRepository;
import com.akshay.OnlineBiddingSystem.Repository.BidRepository;
import com.akshay.OnlineBiddingSystem.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BidService {
    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final AuctionItemRepository auctionItemRepository;

    public BidService(UserRepository userRepository, BidRepository bidRepository, AuctionItemRepository auctionItemRepository ) {
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
        this.auctionItemRepository = auctionItemRepository;
    }

    public void placeBid(Long itemId,Long bidderId,Double amount){

        User bidder = userRepository.findById(bidderId)
                .orElseThrow(()-> new IllegalArgumentException("User not found with this id: "+ bidderId));
        AuctionItem item = auctionItemRepository.findById(itemId)
                .orElseThrow(()-> new IllegalArgumentException("item not found with this id: " + itemId));
        if(LocalDateTime.now().isAfter(item.getExpirationDate())){
            throw new IllegalArgumentException("Cannot be place a bid . This auction has already ended");
        }

        if(item.getSeller().getId().equals(bidderId))
        {
            throw new IllegalArgumentException("Seller cannot  place a bid on his own items ");
        }
        Optional<Bid> highestBid = bidRepository.findTopByAuctionItemIdOrderByBidAmountDesc(itemId);

        double minAmount;
        if(highestBid.isPresent()){
            minAmount = highestBid.get().getBidAmount();
            if(amount<=minAmount)
            {
                throw new IllegalArgumentException("Bid must be greater than current highest  bid:"+minAmount);
            }

        }else {
            minAmount = item.getStartingPrice();
            if (amount < minAmount)
            {
                throw new IllegalArgumentException("Bid amount cannot be lower than item's starting price:"+ minAmount);
            }
        }
        Bid bid = new Bid();
        bid.setBidAmount(amount);
        bid.setBidTime(LocalDateTime.now());
        bid.setBidder(bidder);
        bid.setAuctionItem(item);

        bidRepository.save(bid);

    }

}
