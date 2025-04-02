package dev.franke.felipe.transaction_manager;

import dev.franke.felipe.transaction_manager.api.model.UserModel;
import dev.franke.felipe.transaction_manager.api.repository.UserRepository;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TransactionManagerApplication {

    private final UserRepository userRepository;

    @Value("${custom.user.name}")
    private String userName;

    @Value("${custom.user.password}")
    private String password;

    @Value("${custom.user.email}")
    private String email;

    public TransactionManagerApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-03:00"));
        SpringApplication.run(TransactionManagerApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            if (userRepository.count() <= 0) {
                var encoder = new BCryptPasswordEncoder();
                var user = new UserModel(this.userName, this.password, this.email, "ADMIN");
                user.setActive(true);
                user.setPassword(encoder.encode(user.getPassword()));
                this.userRepository.save(user);
            }
        };
    }
}
