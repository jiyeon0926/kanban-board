package onepick.kanban.card.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import onepick.kanban.card.dto.CardAttachmentDto;
import onepick.kanban.card.entity.Card;
import onepick.kanban.card.entity.CardAttachment;
import onepick.kanban.card.repository.CardAttachmentRepository;
import onepick.kanban.card.repository.CardRepository;
import onepick.kanban.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

//@Service
//@RequiredArgsConstructor
//public class CardAttachmentService {

//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;

//    private final CardAttachmentRepository attachmentRepository;
//    private final CardRepository cardRepository;
//
////    private final S3Client s3Client;
//
//    public List<CardAttachmentDto> createAttachments(Long cardId, List<MultipartFile> files) {
//        Card card = cardRepository.findById(cardId)
//                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다."));
//
//        List<CardAttachment> attachmentList = new ArrayList<>();
//
//        for (MultipartFile file : files) {
//            String originalFilename = file.getOriginalFilename();
//
//            if (originalFilename == null) {
//                continue;
//            }
//
//            String extension = getExtension(originalFilename).toLowerCase();
//            if (!isSupportedFileType(extension)) {
//                throw new IllegalArgumentException("지원되지 않는 파일 형식입니다: " + extension);
//            }
//
//            if (file.getSize() > 5 * 1024 * 1024) { // 5MB 제한
//                throw new IllegalArgumentException("파일 크기가 제한을 초과했습니다.");
//            }
//
//            String imageName = UUID.randomUUID() + "_" + originalFilename;
//
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(bucket)
//                    .key(imageName)
//                    .contentType(file.getContentType())
//                    .build();
//
//            try {
//                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
//            } catch (IOException e) {
//                throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
//            }
//
//            String fileUrl = getPublicUrl(imageName);
//
//            CardAttachment cardAttachment = new CardAttachment(card, fileUrl, originalFilename, extension);
//            attachmentList.add(attachmentRepository.save(cardAttachment));
//        }
//
//        return attachmentList.stream()
//                .map(att -> new CardAttachmentDto(att.getId(), att.getImage(), att.getImageName(), att.getFileType()))
//                .collect(Collectors.toList());
//    }
//
//    private String getExtension(String filename) {
//        return FilenameUtils.getExtension(filename);
//    }
//
//    private boolean isSupportedFileType(String extension) {
//        return List.of("jpg", "jpeg", "png", "pdf", "csv").contains(extension);
//    }
//
//    private String getPublicUrl(String key) {
//        return "https://" + bucket + ".s3.amazonaws.com/" + key;
//    }


//    public List<CardAttachmentDto> getAttachments(Long cardId, User user) {
//        Card card = cardRepository.findById(cardId)
//                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다."));
//
//        List<CardAttachment> attachments = attachmentRepository.findAllByCardId(cardId);
//        return attachments.stream()
//                .map(att -> new CardAttachmentDto(att.getId(), att.getImage(), att.getImageName(), att.getFileType()))
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public void deleteAttachment(Long attachmentId, User user) {
//        CardAttachment attachment = attachmentRepository.findById(attachmentId)
//                .orElseThrow(() -> new NoSuchElementException("첨부파일을 찾을 수 없습니다."));
//
//        // S3에서 파일 삭제
//        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
//                .bucket(bucket)
//                .key(attachment.getImageName())
//                .build();
//        s3Client.deleteObject(deleteObjectRequest);
//
//        attachmentRepository.delete(attachment);
//    }
//}
