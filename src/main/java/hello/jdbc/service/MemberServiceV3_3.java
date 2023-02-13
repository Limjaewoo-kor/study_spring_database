package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 */
@Slf4j
public class MemberServiceV3_3 {

//    private final TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;
    //    public MemberServiceV3_3(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
//        this.txTemplate = new TransactionTemplate(transactionManager);
//        this.memberRepository = memberRepository;
//    }
    public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
        this.memberRepository = memberRepository;
    }



    @Transactional // 이 어노테이션으로 트랜잭션 관련 코드를 모두 생략할 수 있다.
    public void accountTrandsfer(String fromId, String toId, int money) throws SQLException {

//        txTemplate.executeWithoutResult((status) -> {
//            try {
                bizLogic(fromId, toId, money);
//            } catch (SQLException e) {
//                throw new IllegalStateException(e);
//            }
//        });
        //executeWithoutResult 안에서 처리가 되면 커밋 언체크 예외가 터지면 롤백이 모두 수행된다.


    }

    private void bizLogic( String fromId, String toId, int money) throws SQLException {
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
