package vn.feature.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.feature.dtos.UserDTO;
import vn.feature.dtos.UserLoginDTO;
import vn.feature.response.state_response.ResponseData;
import vn.feature.response.state_response.ResponseError;
import vn.feature.service.UserService;
import vn.feature.util.MessageKeys;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@Slf4j(topic = "USER_CONTROLLER")
public class UserController  {

    private final UserService userService;

    @Operation(summary = "Create User", description = "API add new user to database")
    @PostMapping("/register")
    public ResponseData<Long> createUser(@RequestBody @Valid UserDTO userDTO) {
        log.info("Request add user,{} {}", userDTO);
        try {
            long userId = userService.createUser(userDTO);
            log.info("Request add user,{} {}", userDTO.getFullName());

            return new ResponseData<>(HttpStatus.CREATED.value(), MessageKeys.REGISTER_SUCCESS, userId);

        } catch (Exception e) {
            log.info("errorMessage = {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), MessageKeys.ERROR_MESSAGE);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO){
        try{
            String token = userService.login(userLoginDTO.getPhoneNumber(),userLoginDTO.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
