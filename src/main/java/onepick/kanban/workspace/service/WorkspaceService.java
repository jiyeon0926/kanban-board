package onepick.kanban.workspace.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.exception.CustomException;
import onepick.kanban.exception.ErrorCode;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import onepick.kanban.workspace.dto.WorkspaceRequestDto;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.repository.WorkspaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final SlackNotifier slackNotifier;
    private final UserRepository userRepository;

    // 워크스페이스 생성 권한 없는 사용자 예외 처리 구현해야 함.
    @Transactional
    public WorkspaceResponseDto createWorkspace(Long creatorId, WorkspaceRequestDto requestDto) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_CREATOR));
        if(!creator.getRole().equals(Role.STAFF) && !creator.getRole().equals(Role.ADMIN)) {
            throw new CustomException(ErrorCode.INVALID_WORKSPACE_CREATOR);
        }
        Workspace workspace = new Workspace(requestDto.getTitle(), requestDto.getContents());
        workspaceRepository.save(workspace);

        String message = workspace.getTitle() + " 워크스페이스가 생성되었습니다.";
        slackNotifier.sendNotification(message);

        return new WorkspaceResponseDto(workspace.getId(), workspace.getTitle(), workspace.getContents());
    }

    public List<WorkspaceResponseDto> getAllWorkspaces() {
        return workspaceRepository.findAll().stream()
                .map(workspace -> new WorkspaceResponseDto(
                        workspace.getId(),
                        workspace.getTitle(),
                        workspace.getContents()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateWorkspace(Long workspaceId, WorkspaceRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_WORKSPACE_ID));
        if (requestDto == null || requestDto.getTitle() == null || requestDto.getContents() == null) {
            throw new CustomException(ErrorCode.WORKSPACE_NOT_FOUND);
        }
        workspace.update(requestDto.getTitle(), requestDto.getContents());
    }

    @Transactional
    public void deleteWorkspace(Long workspaceId, Long deleterId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORKSPACE_NOT_FOUND));

        User deleter = userRepository.findById(deleterId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_DELETER));

        if (!deleter.getRole().equals(Role.STAFF) && !deleter.getRole().equals(Role.ADMIN)) {
            throw new CustomException(ErrorCode.INVALID_WORKSPACE_DELETER);
        }
        workspaceRepository.delete(workspace);
    }
}