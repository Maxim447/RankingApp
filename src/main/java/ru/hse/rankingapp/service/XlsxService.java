package ru.hse.rankingapp.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.rankingapp.dto.event.EventResultDto;
import ru.hse.rankingapp.entity.EventEntity;
import ru.hse.rankingapp.entity.EventUserLinkEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.Gender;
import ru.hse.rankingapp.enums.CategoryEnum;
import ru.hse.rankingapp.enums.ParticipantsTypeEnum;
import ru.hse.rankingapp.enums.SeparatorEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.utils.FioUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

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
            headerRow.createCell(0).setCellValue("ФИО");
            headerRow.createCell(1).setCellValue("Пол");
            headerRow.createCell(2).setCellValue("Почта участника");
            headerRow.createCell(3).setCellValue("Возраст");
            headerRow.createCell(4).setCellValue("Возрастная категория");
            headerRow.createCell(5).setCellValue("Дисциплина");
            headerRow.createCell(6).setCellValue("Дистанция");
            headerRow.createCell(7).setCellValue("Время");

            // Создаем стиль для прозрачного текста
            CellStyle transparentStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex()); // Белый цвет текста (или цвет фона)
            transparentStyle.setFont(font);

            // Создаем стиль для текстового формата
            CellStyle textStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            textStyle.setDataFormat(format.getFormat("@")); // Текстовый формат

            Cell cell = headerRow.createCell(8);
            cell.setCellValue(event.getEventUuid().toString());
            cell.setCellStyle(transparentStyle);

            ParticipantsTypeEnum participantsType = event.getCompetition().getParticipantsType();
            EnumSet<CategoryEnum> categoryEnums = CategoryEnum.getEnumSetByParticipantsType(participantsType);

            // Заполняем данные участников
            int rowNum = 1;
            for (EventUserLinkEntity eventUserLink : event.getEventUserLinks()) {
                UserEntity user = eventUserLink.getUser();
                LocalDate birthDate = user.getBirthDate();

                long fullUserAge = ChronoUnit.YEARS.between(birthDate, LocalDate.now());

                CategoryEnum category = categoryEnums.stream()
                        .filter(categoryEnum -> categoryEnum.getFrom() <= fullUserAge && categoryEnum.getTo() > fullUserAge)
                        .findFirst()
                        .orElse(null);

                Row row = sheet.createRow(rowNum++);
                createCell(FioUtils.buildFullName(user), textStyle, row, 0);
                createCell(user.getGender().getValue(), textStyle, row, 1);
                createCell(user.getEmail(), textStyle, row, 2);
                createCell(String.valueOf(Period.between(user.getBirthDate(), LocalDate.now()).getYears()), textStyle, row, 3);
                createCell(category == null ? "N/D" : category.getStringValue(), textStyle, row, 4);
                createCell(event.getStyle(), textStyle, row, 5);
                createCell(String.valueOf(event.getDistance()), textStyle, row, 6);
                createCell("", textStyle, row, 7);
                createCell("", textStyle, row, 8);
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

    private void createCell(String value, CellStyle style, Row row, int position) {
        Cell cell = row.createCell(position);
        cell.setCellStyle(style);
        cell.setCellValue(value);
    }

    public List<EventResultDto> parseXlsxTemplate(MultipartFile file) {
        List<EventResultDto> results = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();
            rows.next();

            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (!currentRow.cellIterator().hasNext()) {
                    continue;
                }

                DataFormatter dataFormatter = new DataFormatter();
                EventResultDto event = new EventResultDto()
                        .setFullName(currentRow.getCell(0).getStringCellValue())
                        .setGender(Gender.getGenderByValue(currentRow.getCell(1).getStringCellValue()))
                        .setEmail(currentRow.getCell(2).getStringCellValue())
                        .setGroupCategory(currentRow.getCell(4).getStringCellValue())
                        .setStyle(currentRow.getCell(5).getStringCellValue());

                String age = dataFormatter.formatCellValue(currentRow.getCell(3));
                event.setAge(Integer.parseInt(age));

                String distance = dataFormatter.formatCellValue(currentRow.getCell(6));
                event.setDistance(Integer.parseInt(distance));

                String time = dataFormatter.formatCellValue(currentRow.getCell(7));
                if (time == null) {
                    throw new BusinessException("Не заполнено время заплыва для строчки №" + currentRow.getRowNum(), HttpStatus.BAD_REQUEST);
                }

                String[] splitTime = time.split(SeparatorEnum.SEMICOLON.getValue());

                if (splitTime.length != 3) {
                    throw new BusinessException("Неверно заполнено время заплыва для строчки №"
                            + currentRow.getRowNum() + ". " +
                            "Время должно быть формата HH:mm:ss.SSS, где HH - часы, mm - минуты, " +
                            "ss - секунды, SSS - миллисекунды (Опционально)", HttpStatus.BAD_REQUEST);

                }

                if (splitTime[0].length() == 1) {
                    time = "0" + time;
                }

                event.setTime(LocalTime.parse(time));

                results.add(event);
            }

            workbook.close();
            return results;
        } catch (Exception e) {
            throw new BusinessException("Ошибка при обработке xlsx шаблона", HttpStatus.BAD_REQUEST);
        }
    }
}
