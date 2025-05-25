package vn.feature.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import vn.feature.util.MessageKeys;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    @NotEmpty(message = MessageKeys.CATEGORIES_NAME_REQUIRED)
    private String name;
}
