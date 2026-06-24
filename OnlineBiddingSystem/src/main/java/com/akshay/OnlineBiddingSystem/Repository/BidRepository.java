package com.akshay.OnlineBiddingSystem.Repository;

import com.akshay.OnlineBiddingSystem.Entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid,Long> {
    Optional<Bid> findTopByAuctionItemIdOrderByBidAmountDesc(Long auctionItemId);
    List<Bid> findByAuctionItemIdOrderByBidTimeDesc(Long id);
}
