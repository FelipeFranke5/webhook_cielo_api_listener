package dev.franke.felipe.transaction_manager.api.service;

import dev.franke.felipe.transaction_manager.api.dto.UserRequestDTO;
import dev.franke.felipe.transaction_manager.api.dto.UserResponseDTO;
import dev.franke.felipe.transaction_manager.api.exception.UserAlreadyExistsException;
import dev.franke.felipe.transaction_manager.api.model.UserModel;
import dev.franke.felipe.transaction_manager.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserModelDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public UserModelDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        userRequest.validate();

        if (this.userExists(userRequest)) {
            throw new UserAlreadyExistsException();
        }

        var userRole = "USER";
        var user = new UserModel(userRequest.username(), userRequest.password(), userRequest.email(), userRole);
        var passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var savedUser = userRepository.save(user);
        Assert.notNull(savedUser, "savedUser is null");
        var message = "User saved! Please wait for the Admin to activate your account";
        return new UserResponseDTO(savedUser.getUsername(), message);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    private boolean userExists(UserRequestDTO userRequest) {
        return userRepository.findByEmail(userRequest.email()) != null
                || userRepository.findByName(userRequest.username()) != null;
    }
}
