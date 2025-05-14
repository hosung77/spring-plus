package org.example.expert.domain.todo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoSearchResultResponseDto {
    private String title;          // 일정 제목
    private int managerCount;        // 담당자 수
    private int commentCount;     // 댓글 수

    public TodoSearchResultResponseDto(String title, int managerCount, int commentCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }

}
