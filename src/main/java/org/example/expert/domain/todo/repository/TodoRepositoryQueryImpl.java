package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.QUser;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.ProjectedPayload;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<TodoResponse> searchByCondition(Pageable pageable, LocalDate startDate, LocalDate endDate, String weather) {

        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        List<TodoResponse> content = jpaQueryFactory
                .select(Projections.constructor(
                        TodoResponse.class,
                        todo.id,
                        todo.title,
                        todo.contents,
                        todo.weather,
                        Projections.constructor(
                                UserResponse.class,
                                user.id,
                                user.email
                        ),
                        todo.createdAt,
                        todo.modifiedAt
                ))
                .from(todo)
                .leftJoin(todo.user, user)
                .where(
                        createdAtBetween(startDate,endDate),
                        weatherEq(weather)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = getTotalCount(startDate, endDate, weather);

        return new PageImpl<>(content, pageable, total);
    }

    // 날씨 정보가 있으면 제공된 날씨와 일치하는 날씨를 조회 없으면 null 값
    private BooleanExpression weatherEq(String weather) {
        return StringUtils.hasText(weather) ? QTodo.todo.weather.eq(weather) : null;
    }

    private BooleanExpression createdAtBetween(LocalDate start, LocalDate end) {
        if (start == null && end == null) {
            return null;
        }
        if (start != null && end != null) {
            return QTodo.todo.createdAt.between(start.atStartOfDay(), end.atTime(23, 59, 59));
        }
        if (start != null) {
            return QTodo.todo.createdAt.goe(start.atStartOfDay());
        }
        return QTodo.todo.createdAt.loe(end.atTime(23, 59, 59));
    }

    private long getTotalCount(LocalDate startDate, LocalDate endDate, String weather) {
        Long count = jpaQueryFactory
                .select(QTodo.todo.count())
                .from(QTodo.todo)
                .where(
                        createdAtBetween(startDate, endDate),
                        weatherEq(weather)
                )
                .fetchOne();
        return count != null ? count : 0L;
    }


}
