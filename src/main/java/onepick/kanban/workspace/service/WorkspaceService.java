package onepick.kanban.workspace.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onepick.kanban.common.SlackNotifier;
import onepick.kanban.user.repository.UserRepository;
import onepick.kanban.workspace.dto.WorkspaceRequestDto;
import onepick.kanban.workspace.dto.WorkspaceResponseDto;
import onepick.kanban.workspace.entity.Workspace;
import onepick.kanban.workspace.repository.WorkspaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 워크스페이스 ID입니다."));
        if (requestDto == null || requestDto.getTitle() == null || requestDto.getContents() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "워크스페이스를 찾을 수 없습니다.");
        }
        workspace.update(requestDto.getTitle(), requestDto.getContents());
    }

    @Transactional
    public void deleteWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "워크스페이스를 찾을 수 없습니다."));

        workspaceRepository.delete(workspace);
    }

    public Workspace findMemberByWorkspaceId(Long workspaceId) {
       return workspaceRepository.findMemberByWorkspaceId(workspaceId);
    }
}