package ru.hse.rankingapp.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.entity.EventEntity;
import ru.hse.rankingapp.entity.EventUserLinkEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.utils.FioUtils;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Period;

/**
 * Сервис для работы с excel файлами.
 */
@Service
public class XlsxService {

    /**
     * Сформировать шаблон для заполнения результатов заплыва.
     *
     * @param event сущность заплыва
     * @return xlsx data
     */
    public byte[] generateXlsxTemplateByEvent(EventEntity event) {
        // Создаем новый XLSX файл
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Participants");

            // Создаем заголовок
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ФИО участника");
            headerRow.createCell(1).setCellValue("Почта участника");
            headerRow.createCell(2).setCellValue("Возраст");
            headerRow.createCell(3).setCellValue("Пол");
            headerRow.createCell(4).setCellValue("Время");
            headerRow.createCell(5).setCellValue("Очки");
            headerRow.createCell(6).setCellValue("Место");
            headerRow.createCell(7).setCellValue("Место в категории");

            // Создаем стиль для прозрачного текста
            CellStyle transparentStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex()); // Белый цвет текста (или цвет фона)
            transparentStyle.setFont(font);

            Cell cell = headerRow.createCell(8);
            cell.setCellValue(event.getEventUuid().toString());
            cell.setCellStyle(transparentStyle);

            // Заполняем данные участников
            int rowNum = 1;
            for (EventUserLinkEntity eventUserLink : event.getEventUserLinks()) {
                UserEntity user = eventUserLink.getUser();

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(FioUtils.buildFullName(user));
                row.createCell(1).setCellValue(user.getEmail());
                row.createCell(2).setCellValue(Period.between(user.getBirthDate(), LocalDate.now()).getYears());
                row.createCell(3).setCellValue(user.getGender().getValue());
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue("");
                row.createCell(6).setCellValue("");
                row.createCell(7).setCellValue("");

            }

            // Автоматически подгоняем ширину колонок под содержимое
            for (int i = 0; i < 8; i++) {
                sheet.autoSizeColumn(i);
            }

            // Записываем файл в ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new BusinessException("Ошибка при формировании xlsx шаблона", HttpStatus.BAD_REQUEST);
        }
    }
}
