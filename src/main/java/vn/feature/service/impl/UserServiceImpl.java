package vn.feature.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.feature.components.TranslateMessages;
import vn.feature.dtos.UserDTO;
import vn.feature.exception.payload.DataNotFoundException;
import vn.feature.mapper.UserMapper;
import vn.feature.model.Role;
import vn.feature.model.User;
import vn.feature.repository.RoleRepository;
import vn.feature.repository.UserRepository;
import vn.feature.service.UserService;
import vn.feature.util.MessageKeys;
import vn.feature.util.RoleType;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends TranslateMessages implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
  //  private final PasswordEncoder passwordEncoder;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public User createUser(UserDTO userDTO)  throws DataIntegrityViolationException {
        // register new user
        String phoneNumber = userDTO.getPhoneNumber();
        // kiểm tra xem số điện thoại đã tồn tại hay chưa
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException(
                    translate(MessageKeys.PHONE_NUMBER_EXISTED));
        }
        Role role = roleRepository.findById(userDTO.getRoleId()).
                orElseThrow(() -> new DataNotFoundException(translate(MessageKeys.ROLE_NOT_FOUND)));
        if (role.getName().equalsIgnoreCase(RoleType.ADMIN)) { // không có quyền tạo ADMIN
//            throw new BadCredentialsException(
//                    translate(MessageKeys.CAN_NOT_CREATE_ACCOUNT_ROLE_ADMIN));
        }

        // convert userDTO -> user
        User newUser = userMapper.toUser(userDTO);
        newUser.setRole(role);
        newUser.setActive(true); // mở tài khoản

        // kiểm tra xem nếu có accontId, không yêu cầu passowrd
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
           // String encodedPassword = passwordEncoder.encode(password);
           // newUser.setPassword(encodedPassword);
        }
        return userRepository.save(newUser);
    }
}
