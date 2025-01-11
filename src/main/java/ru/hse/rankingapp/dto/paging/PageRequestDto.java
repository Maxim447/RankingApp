package ru.hse.rankingapp.dto.paging;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageRequest;

/**
 * Класс для работы с пагинацией.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Класс для работы с пагинацией")
public class PageRequestDto extends SortRequestDto {

    @Schema(description = "Номер страницы", defaultValue = "0")
    private int page;

    @Schema(description = "Размер страницы", defaultValue = "15")
    private int size = 15;

    public PageRequest toPageRequest() {
        return PageRequest.of(this.page, this.size, this.toSort());
    }
}
