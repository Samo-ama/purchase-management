package com.example.purchase.management.model;

import java.time.LocalDateTime;
import lombok.Value;


@Value
public class DateRange {

    LocalDateTime start;
    LocalDateTime end;

}
