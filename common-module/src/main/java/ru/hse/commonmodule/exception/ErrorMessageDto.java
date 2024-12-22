package ru.hse.commonmodule.exception;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorMessageDto {

    private String msg;

    private int status;

    private String path;

    private String timestamp;
}
