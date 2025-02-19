package com.example.purchase.management.service;

import org.springframework.stereotype.Component;
import com.example.purchase.management.model.DateRange;

import java.time.LocalDate;


    


@Component
public class DateRangeProvider {

    public DateRange getYesterdayRange() {
        return new DateRange(
            LocalDate.now().minusDays(1).atStartOfDay(),
            LocalDate.now().atStartOfDay()
        );
    }

}
