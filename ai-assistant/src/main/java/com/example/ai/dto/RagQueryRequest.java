package com.example.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RagQueryRequest {

    @NotBlank(message = "问题内容不能为空")
    private String question;
}
