package vn.feature.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.feature.components.TranslateMessages;
import vn.feature.dtos.UserDTO;
import vn.feature.model.User;
import vn.feature.response.ApiResponse;
import vn.feature.response.user.UserRegisterResponse;
import vn.feature.service.UserService;
import vn.feature.util.MessageKeys;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController extends TranslateMessages {

    private final UserService userService;

    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody @Valid UserDTO userDTO,
                                                     BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                List<String> errorMessages = bindingResult.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                        .message(translate(MessageKeys.ERROR_MESSAGE))
                                                .errors(errorMessages.stream()
                                                                .map(this::translate)
                                                                        .toList()).build());
            }

            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .message(translate(MessageKeys.ERROR_MESSAGE))
                        .error(translate(MessageKeys.PASSWORD_NOT_MATCH)).build()
                );
            }

            User newUser = userService.createUser(userDTO);
            return ResponseEntity.ok().body(
                    ApiResponse.builder().success(true)
                            .message(translate(MessageKeys.REGISTER_SUCCESS))
                            .payload(UserRegisterResponse.fromUser(newUser)).build()
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message(translate(MessageKeys.ERROR_MESSAGE)).error(e.getMessage()).build()
            );
        }
    }
}
