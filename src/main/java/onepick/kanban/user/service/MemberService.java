package onepick.kanban.user.service;

import lombok.RequiredArgsConstructor;
import onepick.kanban.user.entity.Member;
import onepick.kanban.user.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }
}
