package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 */
//@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_2 {

//    private final PlatformTransactionManager transactionManager;
    private final TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    public void accountTrandsfer(String fromId, String toId, int money) throws SQLException {

        txTemplate.executeWithoutResult((status) -> {
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
        //executeWithoutResult 안에서 처리가 되면 커밋 언체크 예외가 터지면 롤백이 모두 수행된다.

//        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
//        try{
//
//            //비즈니스 로직
//            bizLogic(fromId, toId, money);
//            log.info("commit");
//            transactionManager.commit(status);
//        }catch (Exception e){
//            log.info("rollback");
//            transactionManager.rollback(status);
//            throw new IllegalStateException(e);
//        }

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
