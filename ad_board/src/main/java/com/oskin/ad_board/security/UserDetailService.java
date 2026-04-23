package com.oskin.ad_board.security;

import com.oskin.ad_board.model.User;
import com.oskin.ad_board.repository.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String mail) {
        Optional<User> optional = userRepository.findByMail(mail);
        if (optional.isEmpty()) throw new UsernameNotFoundException("Mail " + mail + " no found");
        User user = optional.get();
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()),
                user.getPassword(),
                List.of(user.getRole().getRoleName().toAuthority())
        );
    }
}