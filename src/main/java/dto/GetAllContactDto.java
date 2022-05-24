package dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString

public class GetAllContactDto {

    List<ContactDto> contacts;
}
