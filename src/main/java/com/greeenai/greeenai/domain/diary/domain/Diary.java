package com.greeenai.greeenai.domain.diary.domain;

import com.greeenai.greeenai.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	@Builder(access = AccessLevel.PRIVATE)
	private Diary(String content) {
		this.content = content;
	}

	public static Diary create(String content) {
		return Diary.builder()
				.content(content)
				.build();
	}
}
