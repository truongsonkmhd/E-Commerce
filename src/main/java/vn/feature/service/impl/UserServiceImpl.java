package vn.feature.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.feature.components.JwtTokenUtil;
import vn.feature.dtos.UserDTO;
import vn.feature.exception.payload.DataNotFoundException;
import vn.feature.mapper.UserMapper;
import vn.feature.model.Role;
import vn.feature.model.User;
import vn.feature.repository.RoleRepository;
import vn.feature.repository.UserRepository;
import vn.feature.service.UserService;
import vn.feature.util.MessageKeys;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USERSERVICEIMPL")
public class UserServiceImpl  implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long createUser(UserDTO userDTO)  throws DataIntegrityViolationException {
        // register new user
        String phoneNumber = userDTO.getPhoneNumber();
        // kiểm tra xem số điện thoại đã tồn tại hay chưa
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException(
                    MessageKeys.PHONE_NUMBER_EXISTED);
        }

        log.info("USERDTO : ",userDTO);

        // convert userDTO -> user
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId()).build();
        Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException(MessageKeys.ROLE_NOT_FOUND));

//        if (role.getName().equalsIgnoreCase(RoleType.ADMIN)) { // không có quyền tạo ADMIN
//            throw new BadCredentialsException(
//                    translate(MessageKeys.CAN_NOT_CREATE_ACCOUNT_ROLE_ADMIN));
//        }

        newUser.setRole(role);
        newUser.setActive(true); // mở tài khoản

        // kiểm tra xem nếu có accontId, không yêu cầu passowrd
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }

        log.info("Request add user,{} {}",newUser);

        userRepository.save(newUser);

        return newUser.getId();
    }

    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()){
            throw new DataNotFoundException("Invalid phonenumber/passwod");
        }
        User existingUser = optionalUser.get(); // muốn trả JWT token?
        //check password
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
            if(!passwordEncoder.matches(password,existingUser.getPassword())){
                throw new BadCredentialsException("Wrong phone number or password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber,password
        );
        //authenticate with java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}
