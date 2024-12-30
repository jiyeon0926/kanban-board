package onepick.kanban.card.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CardRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private final String contents;

    private final LocalDateTime deadline;

    private final List<Long> assigneeIds; // 다중 담당자 ID

    // 파일을 별도의 파라미터로 처리하고 필요시에 DTO와 함께 전달하는 것이 더 나은 방법
    // 시간 부족으로 DTO에 직접 사용
    private List<MultipartFile> attachments; // 첨부파일 리스트
}
