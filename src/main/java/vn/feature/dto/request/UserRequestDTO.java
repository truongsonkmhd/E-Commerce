package vn.feature.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import vn.feature.dto.validator.EnumPattern;
import vn.feature.dto.validator.EnumValue;
import vn.feature.dto.validator.GenderSubset;
import vn.feature.dto.validator.PhoneNumber;
import vn.feature.util.Gender;
import vn.feature.util.UserStatus;
import vn.feature.util.UserType;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import static vn.feature.util.Gender.*;

@Getter
public class UserRequestDTO implements Serializable {

    @NotBlank(message = "firstName must be not blank") // Khong cho phep gia tri blank
    private String firstName;

    @NotNull(message = "lastName must be not null") // Khong cho phep gia tri null
    private String lastName;

    @Email(message = "email invalid format") // Chi chap nhan nhung gia tri dung dinh dang email
    private String email;

    //@Pattern(regexp = "^\\d{10}$", message = "phone invalid format")
    @PhoneNumber(message = "phone invalid format")
    private String phone;

    @NotNull(message = "dateOfBirth must be not null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOfBirth;

    //@Pattern(regexp = "^male|female|other$", message = "gender must be one in {male, female, other}")
    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    private Gender gender;

    @NotNull(message = "username must be not null")
    private String username;

    private String password;

    @NotNull(message = "type must be not null")
    @EnumValue(name = "type", enumClass = UserType.class)
    private String type;

    @NotEmpty(message = "addresses can not empty")
    private Set<AddressDTO> addresses;

    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|NONE")
    private UserStatus status;

    public UserRequestDTO(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}
