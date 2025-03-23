package vn.feature.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // skip value null
public class ApiResponse<T> {
    private boolean success = false;
    private String message;
    private T payload;
    private List<String> errors;
    private String error;
    private Long id;
}
