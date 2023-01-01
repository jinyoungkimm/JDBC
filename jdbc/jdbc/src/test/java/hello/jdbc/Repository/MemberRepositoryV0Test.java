package hello.jdbc.Repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 rp = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("7",10000);
        rp.save(member);

        //findById
        Member findMember = rp.findById(member.getMemberId());
        log.info("findMember = {} " , findMember);

        //update
        rp.update(member.getMemberId(),20000);
        Member updateMember = rp.findById(member.getMemberId());


        //delete
        rp.delete("6");




    }
}