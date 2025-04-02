package dev.franke.felipe.transaction_manager.api.controller;

import dev.franke.felipe.transaction_manager.api.dto.UserRequestDTO;
import dev.franke.felipe.transaction_manager.api.dto.UserResponseDTO;
import dev.franke.felipe.transaction_manager.api.service.UserModelDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Create and Activate the application users")
public class UserModelDetailsController {

    private final UserModelDetailsService userModelDetailsService;

    public UserModelDetailsController(final UserModelDetailsService userModelDetailsService) {
        this.userModelDetailsService = userModelDetailsService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create User", description = "Create a new User")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "400", description = "Invalid Request Body")
    @ApiResponse(responseCode = "500", description = "Internal Error")
    public ResponseEntity<UserResponseDTO> create(@RequestBody final UserRequestDTO userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userModelDetailsService.createUser(userRequest));
    }
}
