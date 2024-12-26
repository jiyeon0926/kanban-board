package onepick.kanban.workspace.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.entity.User;
import onepick.kanban.user.repository.UserRepository;
import onepick.kanban.workspace.dto.WorkspaceRequestDto;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final SlackNotifier slackNotifier;
    private final UserRepository userRepository;

    @Transactional
    public WorkspaceResponseDto createWorkspace(WorkspaceRequestDto requestDto) {
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
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));
        workspace.update(requestDto.getTitle(), requestDto.getContents());
    }

    @Transactional
    public void deleteWorkspace(Long workspaceId, Long deleterId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));

        User deleter = userRepository.findById(deleterId)
                .orElseThrow(() -> new IllegalArgumentException("삭제 요청 사용자를 찾을 수 없습니다."));

        if (!deleter.getRole().equals(Role.WORKSPACE_ADMIN) && !deleter.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("워크스페이스 삭제 권한이 없는 사용자입니다.");
        }
        workspaceRepository.delete(workspace);
    }
}