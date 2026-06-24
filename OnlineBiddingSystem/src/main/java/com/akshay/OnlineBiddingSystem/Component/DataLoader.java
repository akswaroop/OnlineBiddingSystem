package com.akshay.OnlineBiddingSystem.Component;

import com.akshay.OnlineBiddingSystem.Entity.Role;
import com.akshay.OnlineBiddingSystem.Entity.User;
import com.akshay.OnlineBiddingSystem.Repository.UserRepository;
import com.akshay.OnlineBiddingSystem.Service.AuctionItemService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuctionItemService auctionItemService;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, AuctionItemService auctionItemService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.auctionItemService = auctionItemService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("++++++++++++++++++++++++++++ seeding of user data is started+++++++++++++++++++");
        User user1 = new User();
        user1.setUserName("akshay");
        user1.setFirstName("Akshay");
        user1.setLastName("Kumar");
        user1.setEmail("akshay@example.com");
        user1.setPassword(passwordEncoder.encode("password123"));
        user1.setRole(Role.ROLE_ADMIN);
        userRepository.save(user1); // Saved as User ID: 1

        User user2 = new User();
        user2.setUserName("rahul_bidder");
        user2.setFirstName("Rahul");
        user2.setLastName("Sharma");
        user2.setEmail("rahul@example.com");
        user2.setPassword(passwordEncoder.encode("password123"));
        user2.setRole(Role.ROLE_USER);
        userRepository.save(user2);
        System.out.println("++++++++++++++++++++++++++++++Successfully user data is seeded++++++++++++++++++++++++++");


        auctionItemService.listItem(
                1L, // Seller ID (Akshay)
                "Gaming Laptop RTX 4060",
                "Brand new gaming laptop with 16GB RAM and 512GB SSD.",
                800.0, // Starting Price
                LocalDateTime.now().plusDays(3) // Expiration Date set in the future!
        );

        // Item 2: A vintage watch listing expiring in 5 days
        auctionItemService.listItem(
                1L, // Seller ID (Akshay)
                "Vintage Mechanical Watch 1990",
                "Classic hand-wound watch in perfect working condition.",
                150.0, // Starting Price
                LocalDateTime.now().plusDays(5)
        );
        System.out.println("++++++++++++++++++++++++++++Auction items are seeded successfully +++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++++++++++++++++Database seeding is successfully completed+++++++++++++++++++++++++++++++++++++++++++++");
    }
}
