package vn.feature.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.feature.dtos.UserDTO;
import vn.feature.dtos.UserLoginDTO;
import vn.feature.model.Token;
import vn.feature.model.User;
import vn.feature.response.ApiResponse;
import vn.feature.response.user.LoginResponse;
import vn.feature.response.user.UserRegisterResponse;
import vn.feature.service.UserService;
import vn.feature.service.impl.TokenServiceImpl;
import vn.feature.util.MessageKeys;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@Slf4j(topic = "USER_CONTROLLER")
public class UserController {

    private final UserService userService;

    private final TokenServiceImpl tokenService;


    @Operation(summary = "Create User", description = "API add new user to database")
    @PostMapping("/register")
    public  ResponseEntity<ApiResponse<?>> createUser(@RequestBody @Valid UserDTO userDTO , BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .message((MessageKeys.ERROR_MESSAGE))
                                .errors(errorMessages.stream()
                                        .toList()).build()
                );
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .message((MessageKeys.ERROR_MESSAGE))
                        .error((MessageKeys.PASSWORD_NOT_MATCH)).build()
                );
            }

            User newUser = userService.createUser(userDTO);
            return ResponseEntity.ok().body(
                    ApiResponse.builder().success(true)
                            .message((MessageKeys.REGISTER_SUCCESS))
                            .payload(UserRegisterResponse.fromUser(newUser)).build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message((MessageKeys.ERROR_MESSAGE))
                    .error(e.getMessage()).build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request,
            BindingResult bindingResult
    ){
        try{
            if(bindingResult.hasErrors()){
                List<String> errorMessages = bindingResult.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(
                        ApiResponse.<LoginResponse>builder()
                                .message((MessageKeys.LOGIN_FAILED))
                                .errors(errorMessages.stream()
                                        .toList()).build()
                );
            }

            String tokenGenerator = userService.login(
                    userLoginDTO.getPhoneNumber(),
                    userLoginDTO.getPassword()
            );

            String userAgent = request.getHeader("User-Agent");
            User user = userService.getUserDetailsFromToken(tokenGenerator);
            Token token = tokenService.addTokenEndRefreshToken(user, tokenGenerator, isMobileDevice(userAgent));
            ApiResponse<LoginResponse> apiResponse = ApiResponse.<LoginResponse>builder()
                    .success(true)
                    .message((MessageKeys.LOGIN_SUCCESS))
                    .payload(LoginResponse.builder()
                            .token(token.getToken())
                            .refreshToken(token.getRefreshToken())
                            .build())
                    .build();
            return ResponseEntity.ok().body(apiResponse);

        } catch (Exception e){
            return ResponseEntity.badRequest().body(
                    ApiResponse.<LoginResponse>builder()
                            .message((MessageKeys.LOGIN_FAILED))
                            .error(e.getMessage()).build()
            );        }
    }

    private boolean isMobileDevice(String userAgent) {
        return userAgent.toLowerCase().contains("mobile");
    }
}
