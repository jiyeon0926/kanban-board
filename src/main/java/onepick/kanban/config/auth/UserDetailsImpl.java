package onepick.kanban.config.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import onepick.kanban.user.entity.Role;
import onepick.kanban.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    // 계정의 권한 리스트를 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = this.user.getRole();

        return new ArrayList<>(role.getAuthorities());
    }

    // 사용자의 자격 증명 반환
    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    // 사용자의 자격 증명 반환
    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    // 계정 만료
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명 만료
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화
    @Override
    public boolean isEnabled() {
        return true;
    }
}
