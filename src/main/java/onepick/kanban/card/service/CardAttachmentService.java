package onepick.kanban.card.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onepick.kanban.card.dto.CardAttachmentDto;
import onepick.kanban.card.entity.Card;
import onepick.kanban.card.entity.CardAttachment;
import onepick.kanban.card.repository.CardAttachmentRepository;
import onepick.kanban.card.repository.CardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CardAttachmentService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final CardAttachmentRepository attachmentRepository;
    private final CardRepository cardRepository;
    private final S3Client s3Client;

    /**
     * 첨부파일을 업로드하고 저장
     * @param cardId 카드 ID
     * @param files  업로드할 파일 리스트
     * @return 업로드된 첨부파일의 DTO 리스트
     */
    @Transactional
    public List<CardAttachmentDto> createAttachments(Long cardId, List<MultipartFile> files) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다."));

        List<CardAttachment> attachmentList = new ArrayList<>();

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();

            if (originalFilename == null) {
                continue;
            }

            String extension = getExtension(originalFilename).toLowerCase();
            if (!isSupportedFileType(extension)) {
                throw new IllegalArgumentException("지원되지 않는 파일 형식입니다: " + extension);
            }

            if (file.getSize() > 5 * 1024 * 1024) { // 5MB 제한
                throw new IllegalArgumentException("파일 크기가 제한을 초과했습니다.");
            }

            String uniqueFileName = UUID.randomUUID() + "_" + originalFilename.replaceAll("\\s", "_");

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(uniqueFileName)
                    .contentType(file.getContentType())
//                    .acl(ObjectCannedACL.PUBLIC_READ) // 파일을 공개적으로 읽을 수 있도록 설정
                    .build();

            try {
                s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            } catch (IOException exception) {
                log.error("파일 업로드 중 오류가 발생했습니다: {}", exception.getMessage());
                throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", exception);
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new RuntimeException(exception);
            }

            String fileUrl = getPublicUrl(uniqueFileName);

            CardAttachment cardAttachment = new CardAttachment(card, fileUrl, originalFilename, extension);
            attachmentList.add(attachmentRepository.save(cardAttachment));
        }

        return attachmentList.stream()
                .map(att -> new CardAttachmentDto(att.getId(), att.getImage(), att.getImageName(), att.getFileType()))
                .collect(Collectors.toList());
    }

    /**
     * 카드의 모든 첨부파일을 조회
     * @param cardId 카드 ID
     * @return 첨부파일의 DTO 리스트
     */
    public List<CardAttachmentDto> getAttachments(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("카드를 찾을 수 없습니다."));

        List<CardAttachment> attachments = attachmentRepository.findAllByCardId(cardId);
        return attachments.stream()
                .map(att -> new CardAttachmentDto(att.getId(), att.getImage(), att.getImageName(), att.getFileType()))
                .collect(Collectors.toList());
    }

    /**
     * 첨부파일을 삭제합니다.
     * @param attachmentId 첨부파일 ID
     */
    @Transactional
    public void deleteAttachment(Long attachmentId) {
        CardAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NoSuchElementException("첨부파일을 찾을 수 없습니다."));

        // S3에서 파일 삭제
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(getKeyFromUrl(attachment.getImage()))
                .build();
        s3Client.deleteObject(deleteObjectRequest);

        // 데이터베이스에서 첨부파일 삭제
        attachmentRepository.delete(attachment);
    }

    /**
     * 파일 이름에서 확장자를 추출
     * @param filename 파일 이름
     * @return 확장자
     */
    private String getExtension(String filename) {
        return StringUtils.getFilenameExtension(filename);
    }

    /**
     * 지원되는 파일 형식인지 확인
     * @param extension 파일 확장자
     * @return 지원 여부
     */
    private boolean isSupportedFileType(String extension) {
        return List.of("jpg", "jpeg", "png", "pdf", "csv").contains(extension);
    }

    /**
     * S3 버킷의 공개 URL을 생성
     * @param key S3 객체 키
     * @return 공개 URL
     */
    private String getPublicUrl(String key) {
        return "https://" + bucket + ".s3.amazonaws.com/" + key;
    }

    /**
     * S3 객체 URL에서 키를 추출
     * @param url S3 객체 URL
     * @return S3 객체 키
     */
    private String getKeyFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();
            return Paths.get(path).getFileName().toString();
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("유효하지 않은 URL입니다: " + url, exception);
        }
    }
}




