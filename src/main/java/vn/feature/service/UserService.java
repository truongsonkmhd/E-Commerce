package vn.feature.service;

import vn.feature.dtos.UserDTO;
import vn.feature.model.User;

public interface UserService {
    long createUser(UserDTO userDTO) ;

    String login(String phoneNumber, String password) throws Exception;

}
