package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        // error가 발생해도 폼의 데이터를 그대로 가지고 return 페이지로 간다.
        // thymeleaf는 spring과 결합도가 좋다.
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address((form.getCity()), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    /**
     * 이 또한 Entity를 그대로 노출(전달)하는 것은 좋은 방법이 아니다.
     * DTO를 통해 필요한 정보만 추려서 전달할 것.
     * 1. 보안의 문제가 있다. Entity가 가진 외부에 노출돼서는 안되는 정보
     * 2. APi의 불안정
     */
    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
