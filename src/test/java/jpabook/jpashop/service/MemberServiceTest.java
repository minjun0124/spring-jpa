package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
//    @Autowired
//    EntityManager em;

    @Test
    //@Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        // 이때, insert문이 로그에 찍히지 않는 이유는
        // em.persist만 한다고 DB에 commit까지 되지는 않기 때문이다. (@Transactional 때문(기본적으로 rollback))
        // insert를 확인하고 싶으면 위의 @Rollback(false) 을 걸어줘야 한다.
        // 또는 아래의 em.flush(); 하게 되면 insert까지 반영한 후 @Transactional 로 인해 롤백된다.
        // (영속성 컨텍스트를 '플러쉬' 하지 않는다.)
        Long savedId = memberService.join(member);

        //then
        //em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);

//        try{
//            memberService.join(member2);
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        fail("예외가 발생한다.");
    }

}
