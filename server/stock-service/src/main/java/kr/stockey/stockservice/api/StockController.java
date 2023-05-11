package kr.stockey.stockservice.api;

import kr.stockey.stockservice.api.request.GetCorrelationRequest;
import kr.stockey.stockservice.api.response.GetStockResponse;
import kr.stockey.stockservice.api.response.GetStockTodayResponse;
import kr.stockey.stockservice.client.MemberClient;
import kr.stockey.stockservice.dto.*;
import kr.stockey.stockservice.dto.core.DailyStockDto;
import kr.stockey.stockservice.dto.core.MemberDto;
import kr.stockey.stockservice.dto.core.ResponseDto;
import kr.stockey.stockservice.dto.core.StockDto;
import kr.stockey.stockservice.mapper.StockDtoMapper;
import kr.stockey.stockservice.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;
    private final StockDtoMapper stockDtoMapper;
    private final MemberClient memberClient;
    /*
            3. 산업중에 사이트 내에서 검색된 순위
        * */

    @Operation(summary = "종목 상세 반환", description = "종목 상세를 반환합니다..")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "종목 없음")
            }
    )
    @GetMapping("/{stockId}")
    public ResponseEntity<GetStockResponse> getStock(@PathVariable("stockId") Long stockId)  {
        StockSummaryDto stockDto = stockService.getStock(stockId);
        return ResponseEntity.ok(stockDtoMapper.toGetStockResponse(stockDto));
    }



    @Operation(summary = "종목 리스트", description = "종목의 간결한 설명 및 현재 가격을 반환합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "종목 없음")
            }
    )
    @GetMapping
    public ResponseEntity<List<StockPreviewDto>> getStock()  {
        List<StockPreviewDto> stockPreviewDtos = stockService.getStock();
        return ResponseEntity.ok(stockPreviewDtos);
    }

    @Operation(summary = "랜덤한 종목 리스트 반환", description = "랜덤한 종목의 간결한 설명 및 현재 가격을 반환합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "종목 없음")
            }
    )
    @GetMapping("/random")
    public ResponseEntity<List<StockPreviewDto>> getStockRandom(@RequestParam Integer count)  {
        List<StockPreviewDto> stockPreviewDtos = stockService.getStockRandom(count);
        return ResponseEntity.ok(stockPreviewDtos);
    }

    @Operation(summary = "종목 검색 결과 반환", description = "종목이름 검색 결과를 반환합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<StockSearchDto>> getStockSearch(@RequestParam String keyword)  {
        List<StockSearchDto> stockSearchDtos = stockService.getSearchStock(keyword);
        return ResponseEntity.ok(stockSearchDtos);
    }

    @Operation(summary = "종목과 연관성 있는 키워드 top6", description = "종목과 연관성 있는 키워드 top6을 반환합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "종목 없음")
            }
    )
    @GetMapping("/{stockId}/keyword")
    public ResponseEntity<List<StockKeywordDto>> getStockKeyword(@PathVariable("stockId") Long stockId)  {
        List<StockKeywordDto> keywords = stockService.getStockKeyword(stockId);
        return ResponseEntity.ok(keywords);
    }

    @Operation(summary = "주식 데이터 조회", description = "해당 종목의 주식 데이터 조회(2022.01.01~)")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "종목 없음")
            }
    )
    @GetMapping("/{stockId}/dailystock")
    public ResponseEntity<List<DailyStockDto>> getDailyStock(@PathVariable("stockId") Long stockId) {
        List<DailyStockDto> dailyStockDtos = stockService.getDailyStock(stockId);
        return ResponseEntity.ok(dailyStockDtos);
    }

    // 내 관심종목 리스트
    // TODO api gateway route 추가
    @Operation(summary = "관심 종목 리스트", description = "내 관심 종목 리스트를 출력합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    @GetMapping("/my")
    public ResponseEntity<ResponseDto> getMyIndustries(@RequestHeader String userId) {
        MemberDto memberDto = getMember(userId);
        List<GetStockTodayResponse> myStocks = stockService.getMyStocks(memberDto);
        return new ResponseEntity<>(new ResponseDto("OK", myStocks), HttpStatus.OK);

    }


    // 관심 여부 확인
    // TODO api gateway route 추가
    @Operation(summary = "종목 관심 여부 체크", description = "해당 종목이 관심등록 했는지 체크합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", description = "로그인 필요"),
                    @ApiResponse(responseCode = "404", description = "종목 없음"),
            }
    )
    @GetMapping("/my/{id}")
    public ResponseEntity<ResponseDto> checkFavorite(@RequestHeader String userId,@PathVariable Long id) {
        MemberDto memberDto = getMember(userId);
        boolean result = stockService.checkFavorite(memberDto.getId(), id);
        return new ResponseEntity<>(new ResponseDto("OK", result), HttpStatus.OK);
    }

    // 관심 종목 등록
    // TODO api gateway route 추가
    @Operation(summary = "관심 종목 등록", description = "관심 종목을 등록합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "등록 성공"),
                    @ApiResponse(responseCode = "400", description = "이미 관심 종목 등록"),
                    @ApiResponse(responseCode = "404", description = "종목 없음"),
            }
    )
    @PostMapping("/my/{id}")
    public ResponseEntity<ResponseDto> addFavorite(@RequestHeader String userId, @PathVariable Long id) {
        MemberDto memberDto = getMember(userId);
        stockService.addFavorite(memberDto, id);
        return new ResponseEntity<>(new ResponseDto("CREATED", null), HttpStatus.CREATED);
    }

    // 관심 종목 삭제
    // TODO api gateway route 추가
    @Operation(summary = "관심 종목 삭제", description = "관심 종목을 삭제합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "등록 성공"),
                    @ApiResponse(responseCode = "400", description = "다른 유저, 관심 종목 등록 X"),
                    @ApiResponse(responseCode = "404", description = "종목 없음"),
            }
    )
    @DeleteMapping("/my/{id}")
    public ResponseEntity<ResponseDto> deleteFavorite(@RequestHeader String userId,@PathVariable Long id) {
        MemberDto memberDto = getMember(userId);
        stockService.deleteFavorite(memberDto, id);
        return new ResponseEntity<>(new ResponseDto("DELETED", null), HttpStatus.OK);
    }

//    @Operation(summary = "종목과 키워드 추이 상관분석", description = "종목과 키워드 추이 상관분석 결과를 진행합니다.")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "요청 성공"),
//                    @ApiResponse(responseCode = "404", description = "종목 없음, 키워드 없음"),
//            }
//    )
//
//    // TODO 상관관계 추가
//    @GetMapping("/keyword/correlation/{id}")
//    public ResponseEntity<ResponseDto> getCorrelation(@PathVariable Long id,
//                                                      @Valid @ModelAttribute GetCorrelationRequest getCorrelationRequest){
//        Double correlation = stockService.getCorrelation(id, getCorrelationRequest);
//        System.out.println("correlation = " + correlation);
//        return new ResponseEntity<>(new ResponseDto("OK",correlation),HttpStatus.OK);
//    }

    @Operation(summary = "같은 산업내 종목과 키워드 추이 상관분석", description = "모든종목과 키워드 추이 상관분석 결과를 상위 3개를 보여줍니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", description = "종목 없음, 키워드 없음"),
            }
    )
    @GetMapping("/keyword/correlation/{id}/high")
    public ResponseEntity<ResponseDto> getAllCorrelation(@PathVariable Long id,
                                                      @Valid @ModelAttribute GetCorrelationRequest getCorrelationRequest){
        List<ResultCorrelationDto> top3StockCorrelation = stockService.getAllStockCorrelation(id, getCorrelationRequest);
        return new ResponseEntity<>(new ResponseDto("OK",top3StockCorrelation),HttpStatus.OK);
    }


    @Operation(summary = "산업별 종목", description = "산업별 종목을 출력합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "404", description = "산업 없음"),
            }
    )
    @GetMapping("/industry/{industryId}")
    public ResponseEntity<ResponseDto> getByIndustryId(@PathVariable Long industryId){
        List<StockDto> stockList = stockService.getByIndustryId(industryId);
        return new ResponseEntity<>(new ResponseDto("OK", stockList), HttpStatus.OK);
    }

    @Operation(summary = "시가총액 기준 N개 종목", description = "시가총액 기준으로 N개 종목을 출력")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    @GetMapping("/")
    public ResponseEntity<ResponseDto> getNStock(@RequestParam int page,@RequestParam int size){
        List<StockDto> stockTop5 = stockService.getNStock(page, size);
        return new ResponseEntity<>(new ResponseDto("OK", stockTop5), HttpStatus.OK);
    }



    @Operation(summary = "산업별 날짜별 시가총액합", description = "산업의 시가총액을 날짜별로 출력합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "성공"),
            }
    )
    @GetMapping("/marketcap-by-date/industry/{industryId}")
    public ResponseEntity<ResponseDto> getMarketList(@PathVariable Long industryId){
        List<IndustrySumDto> marketList = stockService.getMarketList(industryId);
        return new ResponseEntity<>(new ResponseDto("OK", marketList), HttpStatus.OK);
    }



    private MemberDto getMember(String userId) {
        ResponseDto responseDto = memberClient.getMember(userId);
        MemberDto memberDto = (MemberDto) responseDto.getData();
        return memberDto;

    }


}
