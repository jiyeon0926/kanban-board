package onepick.kanban.workspace.entity;

import lombok.Getter;

@Getter
public enum Status {
    PENDING,
    ACCEPTED,
    REJECTED,
    EXPIRED,
    CANCELLED
}
