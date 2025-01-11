package ru.hse.rankingapp.dto.paging;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Sort;

/**
 * Класс для работы с сортировкой.
 */
@Data
@Schema(description = "Класс для работы с сортировкой")
public class SortRequestDto {

    @Schema(description = "Наименование параметра", defaultValue = "id")
    private String property;

    @Schema(description = "Направление сортировки")
    private Sort.Direction direction;

    @Schema(description = "Порядок вывода null значений")
    private Sort.NullHandling nullHandling;

    public Sort toSort() {
        if (this.property != null) {
            Sort.Order order = Sort.Order.by(this.property).with(this.direction).with(this.nullHandling);
            return Sort.by(order);
        }

        return Sort.unsorted();
    }

    public SortRequestDto() {
        this.direction = Sort.DEFAULT_DIRECTION;
        this.nullHandling = Sort.NullHandling.NATIVE;
    }
}
