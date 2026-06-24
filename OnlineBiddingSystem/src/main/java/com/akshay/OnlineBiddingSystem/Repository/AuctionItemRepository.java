package com.akshay.OnlineBiddingSystem.Repository;

import com.akshay.OnlineBiddingSystem.Entity.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionItemRepository extends JpaRepository<AuctionItem,Long> {
}
