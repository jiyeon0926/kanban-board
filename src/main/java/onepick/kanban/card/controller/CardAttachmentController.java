package onepick.kanban.card.controller;

import lombok.RequiredArgsConstructor;
import onepick.kanban.card.dto.CardAttachmentDto;
import onepick.kanban.card.service.CardAttachmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/cards/{cardId}/attachments")
@RequiredArgsConstructor
public class CardAttachmentController {

    private final CardAttachmentService attachmentService;

    /**
     * 첨부파일 업로드
     * @param cardId 카드 ID
     * @param files  업로드할 파일 리스트
     * @return 업로드된 첨부파일의 DTO 리스트
     */
    @PostMapping
    public ResponseEntity<List<CardAttachmentDto>> uploadAttachments(
            @PathVariable Long cardId,
            @RequestParam("files") List<MultipartFile> files) {

        // 첨부파일 업로드 및 저장
        List<CardAttachmentDto> uploadedAttachments = attachmentService.createAttachments(cardId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedAttachments);
    }

    /**
     * 첨부파일 조회
     * @param cardId 카드 ID
     * @return 첨부파일의 DTO 리스트
     */
    @GetMapping
    public ResponseEntity<List<CardAttachmentDto>> getAttachments(
            @PathVariable Long cardId) {

        List<CardAttachmentDto> attachments = attachmentService.getAttachments(cardId);
        return ResponseEntity.ok(attachments);
    }

    /**
     * 첨부파일 삭제
     * @param cardId 카드 ID
     * @param attachmentId 첨부파일 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long cardId,
            @PathVariable Long attachmentId) {

        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.noContent().build();
    }
}
