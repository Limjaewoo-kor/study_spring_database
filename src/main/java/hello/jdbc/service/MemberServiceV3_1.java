package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_1 {

//    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;



    public void accountTrandsfer(String fromId,String toId, int money) throws SQLException {

//        Connection con = dataSource.getConnection();

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
//            con.setAutoCommit(false);

            //비즈니스 로직
            bizLogic(fromId, toId, money);
            log.info("commit");
//            con.commit();// 성공시 커밋
            transactionManager.commit(status);
        }catch (Exception e){
            log.info("rollback");
            transactionManager.rollback(status);
//            con.rollback(); //실패시 롤백
            throw new IllegalStateException(e);
        }finally {
//            release(con); transactionManager 내부에서 자동으로 해줌
        }



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
