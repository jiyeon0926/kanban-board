package onepick.kanban.workspace.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public WorkspaceResponseDto createWorkspace(WorkspaceRequestDto requestDto) {
        Workspace workspace = new Workspace(requestDto.getTitle(), requestDto.getContents());
        workspaceRepository.save(workspace);
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
    public void deleteWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new IllegalArgumentException("워크스페이스를 찾을 수 없습니다."));
        workspaceRepository.delete(workspace);
    }
}