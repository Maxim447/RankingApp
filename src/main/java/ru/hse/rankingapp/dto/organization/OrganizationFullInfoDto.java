package ru.hse.rankingapp.dto.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.dto.competition.CompetitionInfoDto;
import ru.hse.rankingapp.dto.user.UserInfoDto;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "Полная информация об организации")
public class OrganizationFullInfoDto {

    @Schema(description = "Идентификатор")
    private Long id;

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "Наименование организации")
    private String name;

    @Schema(description = "Признак открытости организации (true -> открытая, false -> закрытая)")
    private Boolean isOpen;

    @Schema(description = "Ссылка на фото")
    private String image;

    @Schema(description = "Информация о пользователях")
    private List<UserInfoDto> users;

    @Schema(description = "Информация о соревнованиях")
    private List<CompetitionInfoDto> competitions;
}
