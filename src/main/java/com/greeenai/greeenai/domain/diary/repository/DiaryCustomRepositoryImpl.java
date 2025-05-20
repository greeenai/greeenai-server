package com.greeenai.greeenai.domain.diary.repository;

import static com.greeenai.greeenai.domain.diary.domain.QDiary.*;

import com.greeenai.greeenai.domain.diary.domain.Diary;
import com.greeenai.greeenai.domain.member.domain.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Diary> findAllByMemberAndEntryDate(Member member, @Nullable LocalDate entryDate) {
        return queryFactory
                .selectFrom(diary)
                .where(diary.member.eq(member), eqEntryDate(entryDate))
                .orderBy(diary.entryDate.desc())
                .fetch();
    }

    private BooleanExpression eqEntryDate(LocalDate entryDate) {
        return entryDate != null ? diary.entryDate.eq(entryDate) : null;
    }
}
