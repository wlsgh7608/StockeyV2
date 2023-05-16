package kr.stockey.investmentservice.dto;

import lombok.Data;

@Data
public class MyStockInfoDto {
    private Long stockId;
    private String stockName;
    private Double svp; // 주식 평가액 비중 (%)
    private Double rrp; // 수익률 (%)
    private Long curStockPrice; // 주식 현재 가격 *
    private Double avgPrice; // 주식 평단 *
    private Long count; // 보유중인 주식 수 *
}
