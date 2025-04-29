package com.greeenai.greeenai.domain.diary.domain;

import com.greeenai.greeenai.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false)
	private Question question;

	@Builder(access = AccessLevel.PRIVATE)
	private Answer(String content, Question question) {
		this.content = content;
		this.question = question;
	}

	public static Answer create(String content, Question questio) {
		return Answer.builder()
				.content(content)
				.question(questio)
				.build();
	}
}
