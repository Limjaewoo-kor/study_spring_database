package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * 예외 누수 문제 해결
 * SQLException 제거
 *
 * MemberRepository 인터페이스 의존
 */
@Slf4j
public class MemberServiceV4 {

    private final MemberRepository memberRepository;


    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }



    @Transactional // 이 어노테이션으로 트랜잭션 관련 코드를 모두 생략할 수 있다.
    public void accountTrandsfer(String fromId, String toId, int money) {

        bizLogic(fromId, toId, money);

    }

    private void bizLogic( String fromId, String toId, int money) {
        Member fromMember = memberRepository.findById( fromId);
        Member toMember = memberRepository.findById( toId);

        memberRepository.update(fromId,fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
