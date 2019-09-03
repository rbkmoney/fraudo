package com.rbkmoney.fraudo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeWindow {

    private Long startWindowTime;
    private Long endWindowTime;

}
