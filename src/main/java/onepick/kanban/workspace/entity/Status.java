package onepick.kanban.workspace.entity;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    EXPIRED("expired"),
    CANCELLED("cancelled");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public static Status of(String statusName) {
        for (Status status : values()) {
            if (status.getName().equals(statusName)) {
                return status;
            }
        }

        throw new IllegalArgumentException("해당하는 이름의 상태를 찾을 수 없습니다: " + statusName);
    }
}
