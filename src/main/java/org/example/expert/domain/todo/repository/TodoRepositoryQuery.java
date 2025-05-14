package org.example.expert.domain.todo.repository;


import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResultResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TodoRepositoryQuery {

    Page<TodoResponse> searchByCondition(Pageable pageable, LocalDate start, LocalDate end, String weather);

    TodoResponse searchById(Long todoId);

    Page<TodoSearchResultResponseDto> searchByKey(String titleKeyword, String nicknameKeyword,
                                                  LocalDate startDate, LocalDate endDate,
                                                  Pageable pageable);
}
