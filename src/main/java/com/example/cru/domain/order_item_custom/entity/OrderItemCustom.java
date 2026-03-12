package com.example.cru.domain.order_item_custom.entity;

import com.example.cru.domain.order_item.entity.OrderItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "order_item_customs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
public class OrderItemCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 단방향 @OneToOne — OrderItemCustom이 OrderItem을 알고, OrderItem은 이를 모름
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem;

    private String playerName;   // 선수 이름

    private String number;       // 등번호

    private String emblemImageUrl; // 엠블럼 이미지 URL (S3)

    private boolean patchOption; // 패치 여부

    private String sleeveOption; // 원단 옵션

    private String frontImageUrl; // 앞면 커스텀 이미지 (S3)

    private String backImageUrl;  // 뒷면 커스텀 이미지 (S3)

    @Builder
    public OrderItemCustom(Long id, OrderItem orderItem, String playerName, String number, String emblemImageUrl, boolean patchOption, String sleeveOption, String frontImageUrl, String backImageUrl) {
        this.id = id;
        this.orderItem = orderItem;
        this.playerName = playerName;
        this.number = number;
        this.emblemImageUrl = emblemImageUrl;
        this.patchOption = patchOption;
        this.sleeveOption = sleeveOption;
        this.frontImageUrl = frontImageUrl;
        this.backImageUrl = backImageUrl;
    }
}
