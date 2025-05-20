package com.greeenai.greeenai.domain.diary.dto.response;

import com.greeenai.greeenai.domain.diary.domain.Option;

public record OptionResponse(Long id, String content) {
    public static OptionResponse from(Option option) {
        return new OptionResponse(option.getId(), option.getContent());
    }
}
