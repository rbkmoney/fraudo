package com.rbkmoney.fraudo.model;

import com.rbkmoney.fraudo.constant.CheckedField;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupByModel {

    private List<CheckedField> checkedFields;

}
