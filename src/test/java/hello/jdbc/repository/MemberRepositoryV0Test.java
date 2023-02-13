package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @AfterEach
    void aa() {

    }

    @Test
    void crud() throws SQLException {

        Member memberV0 = new Member("memberV0", 10000);
        repository.save(memberV0);

        Member findMember = repository.findById(memberV0.getMemberId());
        Assertions.assertThat(findMember).isEqualTo(memberV0);

        repository.update(memberV0.getMemberId(),20000);
        Member updateMember = repository.findById(memberV0.getMemberId());
        Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);

        repository.delete(memberV0.getMemberId());
        Assertions.assertThatThrownBy(() -> repository.findById(memberV0.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);



    }
}