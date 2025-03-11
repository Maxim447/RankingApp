package ru.hse.rankingapp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.hse.rankingapp.dto.competition.CompetitionInfoDto;
import ru.hse.rankingapp.dto.event.EventInfoDto;
import ru.hse.rankingapp.dto.organization.OrganizationInfoDto;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.LocalDate;
import java.util.List;

/**
 * Полная информация о пользователе.
 */
@Data
@Accessors(chain = true)
@Schema(description = "Полная информация о пользователе")
public class UserFullInfoDto {

    @Schema(description = "Почта")
    private String email;

    @Schema(description = "Телефон пользователя")
    private String phone;

    @Schema(description = "Телефон по которому можно позвонить в случае чего")
    private String emergencyPhone;

    @Schema(description = "Дата рождения")
    private LocalDate birthDate;

    @Schema(description = "Пол (MALE/FEMALE)")
    private Gender gender;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Отчество")
    private String middleName;

    @Schema(description = "Рейтинг участника")
    private Double rating;

    @Schema(description = "Соревнования на которые записался пользователь")
    private List<CompetitionInfoDto> userCompetitions;

    @Schema(description = "Заплыв в которых участвует пользователь")
    private List<EventInfoDto> userEvents;

    @Schema(description = "Организации в которых состоит пользователь")
    private List<OrganizationInfoDto> userOrganizations;

    @Schema(description = "Ссылка на фото")
    private String image;
}
