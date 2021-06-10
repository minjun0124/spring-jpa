package jpabook.jpashop.service;

import jpabook.jpashop.OrderSearch;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

//        여기 저기서 객체를 생성하여 사용하면 유지보수 측면에서 좋지 않다.
//        protected로 막아서 사용하지 못하게 한다.
//        OrderItem orderItem1 = new OrderItem();

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        // 위의 객체들이랑 persist를 사용하는 lifecycle이 완전히 같기 때문에
        // (persist할 때, 같이 persist를 걸어야 할 때)
        // CASCADE 옵션을 걸어 save를 한번만 사용할 수 있다.
        // 다만 각 객체를 여기저기서 사용하고 중요한 엔티티가 있을 경우에는
        // 따로 save -> persist로 사용해주는 것이 좋다.
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        // jpa의 강점 : 원래는 관련 컬럼에 해당하는 필드를 다 찾아서 쿼리를 날려줘야 하지만
        // jpa는 변경 사항을 감지하여 (dirty check) 각각 필요한 쿼리를 날려준다.
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
