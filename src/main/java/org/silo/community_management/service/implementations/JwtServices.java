package org.silo.community_management.service.implementations;

import org.silo.community_management.data.model.JwtToken;
import org.silo.community_management.data.model.User;
import org.silo.community_management.data.repo.JwtTokenRepo;
import org.silo.community_management.data.repo.UserRepo;
import org.silo.community_management.dtos.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtServices implements UserDetailsService {

    private final JwtTokenRepo jwtTokenRepository;

    private final JwtUtil jwtUtil;

    private  final UserRepo userRepo;




    public JwtServices(JwtTokenRepo jwtTokenRepository, JwtUtil jwtUtil, UserRepo userRepo) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    // Generate and store the JWT token in the database
    public JwtToken generateAndSaveToken(String userId) {
        String token = jwtUtil.generateToken(userId);

        // Create a new JwtToken object to store in DB
        JwtToken jwtToken = new JwtToken();
        jwtToken.setUserName(userId);
        jwtToken.setToken(token);
        jwtToken.setExpirationTime(System.currentTimeMillis() + 168 * 60 * 60 * 1000);  // Expiration time in milliseconds

        return jwtTokenRepository.save(jwtToken);
    }

    // Find the JWT token by username
    public JwtToken findByUserId(String userId) {
        return jwtTokenRepository.findByUserName(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepo.findUserById(userId)
                .orElseThrow(()-> new UsernameNotFoundException(userId));
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getId(),
                user.getPassword(),
                new ArrayList<>() // Add authorities if needed
        );
    }

}
