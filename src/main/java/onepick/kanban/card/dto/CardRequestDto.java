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

    private List<String> attachmentUrls; // 첨부파일 URL 리스트
}
