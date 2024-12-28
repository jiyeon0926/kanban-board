//package onepick.kanban.card.controller;
//
//import lombok.RequiredArgsConstructor;
//import onepick.kanban.card.dto.CardAttachmentDto;
//import onepick.kanban.card.service.CardAttachmentService;
//import onepick.kanban.user.entity.User;
//import onepick.kanban.user.repository.UserRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/cards/{cardId}/attachments")
//@RequiredArgsConstructor
//public class CardAttachmentController {
//
//    private final CardAttachmentService attachmentService;
//    private final UserRepository userRepository;
//
//    // 첨부파일 조회
//    @GetMapping
//    public ResponseEntity<List<CardAttachmentDto>> getAttachments(@PathVariable Long cardId, @AuthenticationPrincipal User user) {
//        // 권한 검증 로직 추가
//        List<CardAttachmentDto> attachments = attachmentService.getAttachments(cardId, user);
//        return ResponseEntity.ok(attachments);
//    }
//
//    // 첨부파일 삭제
//    @DeleteMapping("/{attachmentId}")
//    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId, @AuthenticationPrincipal User user) {
//        attachmentService.deleteAttachment(attachmentId, user);
//        return ResponseEntity.noContent().build();
//    }
//}
