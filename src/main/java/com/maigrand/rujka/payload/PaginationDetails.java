package com.maigrand.rujka.payload;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PaginationDetails<T> {

    private final List<T> items;

    private final PaginationMetaDetails meta = new PaginationMetaDetails();

    public PaginationDetails(Page<T> page) {
        items = page.getContent();

        meta.setTotalCount(page.getTotalElements());
        meta.setPageCount(page.getTotalPages());
        meta.setCurrentPage(page.getPageable().getPageNumber());
        meta.setPerPage(page.getPageable().getPageSize());
    }
}