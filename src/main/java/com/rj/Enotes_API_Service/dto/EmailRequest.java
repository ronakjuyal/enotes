package com.rj.Enotes_API_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EmailRequest {

    private String to;

    private String subject;

    private String title;
    private String message;

}
