package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 *  * Entity를 그대로 사용해도 되지만 이렇게 form 객체를 만드는 이유?
 *
 *   JPA에서 Entity는 최대한 순수한 그대로 유지를 하는 것이 중요하다.
 *   처음에는 큰 차이를 못 느낄 수 있으나 WEB FRONT 단과 연동되면서
 *   점점 화면에 대해 종속성을 가지게 되고 Entity가 지져분해지며
 *   유지보수성을 떨어뜨린다.
 *   Entity : 핵심 비즈니스 로직 보유
 *   Form, DTO(데이터 전송 객체) : 화면 관련 로직은 이쪽에 배치한다.
 */

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;

}
