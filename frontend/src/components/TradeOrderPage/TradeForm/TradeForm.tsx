import styled from "styled-components"

import TradeFormBalance from "./TradeFormBalance"
import Grid from "@mui/material/Grid"
import TradeStockList from "./TradeStockList"
import TradeBasketList from "./TradeBasketList"
import { CustomDragLayer } from "../../common/DragDrop/CustomDragLayer"
import { useEffect, useMemo, useState } from "react"
import dayjs from "dayjs"

import TradeQuantityInputModal from "./TradeQuantityInputModal"
import { SimpleDialogProps } from "./TradeQuantityInputModal"
import { Button } from "@mui/material"

export type BasketList = {
  id: number
  name: string
  quantity: number
  currentPrice: number
  myStockNums?: number
  time: string
}

const TradeForm = () => {
  // 더미데이터,,,,, 정각 단위로 useQuery써서 가져와야함
  const myBalance = 1000000

  // 판매 목록
  const [sellList, setSellList] = useState<BasketList[]>(
    localStorage.getItem("sellList")
      ? JSON.parse(localStorage.getItem("sellList")!)
      : []
  )
  // 구매 목록
  const [buyList, setBuyList] = useState<BasketList[]>(
    localStorage.getItem("buyList")
      ? JSON.parse(localStorage.getItem("buyList")!)
      : []
  )

  const [time, setTime] = useState<string | undefined>()
  const [modalData, setModalData] = useState<SimpleDialogProps | undefined>()

  const modalDataHandler = (info: SimpleDialogProps) => {
    setModalData(info)
  }
  const currentTime = useMemo(() => {
    return dayjs().format("H")
  }, [time])

  const listHandler = (status: string) => {
    if (status === "팔래요") {
      setSellList(
        localStorage.getItem("sellList")
          ? JSON.parse(localStorage.getItem("sellList")!)
          : []
      )
    } else {
      setBuyList(
        localStorage.getItem("buyList")
          ? JSON.parse(localStorage.getItem("buyList")!)
          : []
      )
    }
  }

  return (
    <>
      <Header>주문서 작성하기</Header>
      <TradeFormContainer container columns={13} justifyContent="center">
        <TradeFormWrapper item direction="column" md={6} xs={12}>
          <TradeFormBalance myBalance={myBalance} />
          <TradeStockList />
        </TradeFormWrapper>
        <TradeFormWrapper item md={6} xs={12}>
          <TradeBasketList
            status={"팔래요"}
            text={"수익"}
            color={"--custom-blue"}
            data={sellList}
            myBalance={myBalance}
            modalDataHandler={modalDataHandler}
            listHandler={listHandler}
          />
          <TradeBasketList
            status={"살래요"}
            text={"지출"}
            color={"--custom-pink-4"}
            data={buyList}
            myBalance={myBalance}
            modalDataHandler={modalDataHandler}
            listHandler={listHandler}
          />
        </TradeFormWrapper>
        <ButtonConfirmComp variant="contained">
          주문서 제출하기
          <ConfirmImage src={"/tradeLogos/paymentCheck.png"} />
        </ButtonConfirmComp>
      </TradeFormContainer>
      <TradeQuantityInputModal
        id={modalData?.id}
        status={modalData?.status}
        stockInfo={modalData?.stockInfo}
        open={modalData?.open}
        modalDataHandler={modalDataHandler}
        listHandler={listHandler}
      />
      <CustomDragLayer />
    </>
  )
}

export default TradeForm
const TradeFormContainer = styled(Grid)`
  min-height: 90vh;
  max-height: 90vh;
  gap: 32px;

  @media (max-width: 900px) {
    min-height: 220vh;
    max-height: 260vh;
  }
`
const TradeFormWrapper = styled(Grid)`
  min-height: 80vh;
  max-height: 80vh;
  width: 100%;

  @media (max-width: 900px) {
    min-height: 120vh;
    max-height: 120vh;
  }
`
const Header = styled.p`
  font-size: 32px;
  font-weight: bold;
`
const ButtonConfirmComp = styled(Button)`
  width: 100%;
  height: 7rem;
  background-color: #625b71 !important;
  border-radius: 96px !important;
  font-size: 32px !important;
  font-weight: bold !important;
`
const ConfirmImage = styled.img``
