package com.hipo.dataobjcet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCheckDto {

    private int Checked;

    private int unChecked;
}
