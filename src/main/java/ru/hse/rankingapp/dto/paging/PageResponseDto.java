package ru.hse.rankingapp.dto.paging;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Класс ответа для работы с пагинацией.
 */
@Data
@Schema(description = "Класс ответа для работы с пагинацией")
public class PageResponseDto<T> {

    @Schema(description = "Общее кол-во элементов")
    private long totalElements;

    @Schema(description = "Общее кол-во страниц")
    private int totalPages;

    @Schema(description = "Содержимое страницы")
    private List<T> content;

    public PageResponseDto() {
    }

    public PageResponseDto(Long totalElements, int totalPages, List<T> content) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }
}
