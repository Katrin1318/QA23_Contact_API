package dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ErrorDto {

    int code;
    String details;
    String message;
}
