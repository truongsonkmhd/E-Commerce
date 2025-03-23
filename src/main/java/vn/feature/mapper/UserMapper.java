package vn.feature.mapper;


import org.mapstruct.Mapper;
import vn.feature.dtos.UserDTO;
import vn.feature.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserDTO userDTO);
}
