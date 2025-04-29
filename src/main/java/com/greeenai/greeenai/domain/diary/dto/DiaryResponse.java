package com.greeenai.greeenai.domain.diary.dto;

import com.greeenai.greeenai.domain.diary.domain.Diary;

public record DiaryResponse(
		Long id,
		String content
) {
	public static DiaryResponse from(Diary diary) {
		return new DiaryResponse(diary.getId(), diary.getContent());
	}
}
