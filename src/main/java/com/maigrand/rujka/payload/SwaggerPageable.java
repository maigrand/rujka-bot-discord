package com.maigrand.rujka.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SwaggerPageable {

    @ApiModelProperty(value = "Номер страницы", allowEmptyValue = true)
    private int page;

    @ApiModelProperty(value = "Количество элементов на одной странице", allowEmptyValue = true)
    private int perPage;
}
