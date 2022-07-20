package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    // 상품코드

    @Column(nullable = false)
    private String itemNm;  // 상품명

    @Column(name="price", nullable = false)
    private int price;  // 가격

    @Column(nullable = false)
    private int stockNumber;    // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  // 상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    /**
     * 상품재고를 감소시키는 메서드 (엔티티 클래스 안에 비즈니스 로직을 작성 -> 코드 재사용성 증가 + 데이터 변경포인트를 한군데서 관리)
     * 현재고수량이 파라미터로 들어온 수량보다 적으면 오류메세지를 넘긴다.
     * 재고수량이 0 이 되면 상품판매상태를 품절로 변경한다.
     * @param stockNumber 재고수량
     */
    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        } else if (restStock == 0) {
            itemSellStatus = ItemSellStatus.SOLD_OUT;
        }
        this.stockNumber = restStock;
    }

    /**
     * 상품재고를 더해주는 메서드
     * 제품이 품절상태라면 판매중으로 변경한다.
     * @param stockNumber
     */
    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
        if (this.itemSellStatus == ItemSellStatus.SOLD_OUT) {
            itemSellStatus = ItemSellStatus.SELL;
        }
    }


}
