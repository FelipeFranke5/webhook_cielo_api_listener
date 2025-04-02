package dev.franke.felipe.transaction_manager.api.dto;

import dev.franke.felipe.transaction_manager.api.exception.EmailHasInvalidRegexException;
import dev.franke.felipe.transaction_manager.api.exception.EmailRequiredException;
import dev.franke.felipe.transaction_manager.api.exception.PasswordHasInvalidLengthException;
import dev.franke.felipe.transaction_manager.api.exception.PasswordHasInvalidRegexException;
import dev.franke.felipe.transaction_manager.api.exception.PasswordRequiredException;
import dev.franke.felipe.transaction_manager.api.exception.UsernameHasInvalidLengthException;
import dev.franke.felipe.transaction_manager.api.exception.UsernameHasInvalidRegexException;
import dev.franke.felipe.transaction_manager.api.exception.UsernameRequiredException;
import java.util.regex.Pattern;

public record UserRequestDTO(String username, String password, String email) {

    public void validate() {
        this.validateUserName();
        this.validatePassword();
        this.validateEmail();
    }

    private boolean userNameIsValid() {
        if (this.userNameIsNullOrEmpty()) {
            throw new UsernameRequiredException();
        }

        if (!this.userNameHasValidLength()) {
            throw new UsernameHasInvalidLengthException();
        }

        var pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$");
        var matcher = pattern.matcher(this.username);
        return matcher.matches();
    }

    private boolean userNameIsNullOrEmpty() {
        return this.username.isBlank();
    }

    private boolean userNameHasValidLength() {
        return this.username.length() > 5 && this.username.length() < 30;
    }

    private void validateUserName() {
        if (!this.userNameIsValid()) {
            throw new UsernameHasInvalidRegexException();
        }
    }

    private boolean passwordIsValid() {
        if (this.passwordIsNullOrBlank()) {
            throw new PasswordRequiredException();
        }

        if (!this.passwordHasValidLength()) {
            throw new PasswordHasInvalidLengthException();
        }

        var pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$");
        var matcher = pattern.matcher(this.password);
        return matcher.matches();
    }

    private boolean passwordIsNullOrBlank() {
        return this.password.isBlank();
    }

    private boolean passwordHasValidLength() {
        return this.password.length() > 8 && this.password.length() < 30;
    }

    private void validatePassword() {
        if (!this.passwordIsValid()) {
            throw new PasswordHasInvalidRegexException();
        }
    }

    private boolean emailIsValid() {
        if (this.emailIsNullOrBlank()) {
            throw new EmailRequiredException();
        }

        var pattern = Pattern.compile("^(?=.{1,254}$)(?=.{1,64}@)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        var matcher = pattern.matcher(this.email);
        return matcher.matches();
    }

    private boolean emailIsNullOrBlank() {
        return this.email.isBlank();
    }

    private void validateEmail() {
        if (!this.emailIsValid()) {
            throw new EmailHasInvalidRegexException();
        }
    }
}
