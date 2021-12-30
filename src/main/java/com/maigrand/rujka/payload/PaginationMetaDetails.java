package com.maigrand.rujka.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationMetaDetails {

    @ApiModelProperty(example = "1")
    private long totalCount;

    @ApiModelProperty(example = "1")
    private int pageCount;

    @ApiModelProperty(example = "1")
    private int currentPage;

    @ApiModelProperty(example = "100")
    private int perPage;
}
