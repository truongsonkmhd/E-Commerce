package vn.feature.service;

import vn.feature.dtos.UserDTO;
import vn.feature.model.User;

public interface UserService {
    User createUser(UserDTO userDTO) throws  Exception;
}
