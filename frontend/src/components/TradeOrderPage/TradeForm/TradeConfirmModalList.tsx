import { useState } from "react"
import styled from "styled-components"

import { useDrop } from "react-dnd"
import { BasketList } from "./TradeForm"
import TradeConfirmModalItem from "./TradeConfirmModalItem"
import { hover } from "@testing-library/user-event/dist/hover"

interface Props {
  status: string
  list: BasketList[]
}
const TradeConfirmModalList = ({ status, list }: Props) => {
  const [items, setItems] = useState<BasketList[]>(list)

  const moveCardHandler = (dragIndex: number, hoverIndex: number) => {
    const dragItem = items[dragIndex]

    if (dragItem) {
      setItems((prevState) => {
        const coppiedStateArray = [...prevState]
        const prevItem = coppiedStateArray.splice(hoverIndex, 1, dragItem)
        coppiedStateArray.splice(dragIndex, 1, prevItem[0])
        return coppiedStateArray
      })
    }
  }

  const [{ getItem }, dropRef] = useDrop(
    () => ({
      accept: ["MYSELL"],
      collect: (monitor) => ({
        getItem: monitor.getItem() as {
          index: number
          name: string
          quantity: number
          status: string
        },
      }),
    }),
    []
  )
  return (
    <OrderList ref={status === "MYSELL" ? dropRef : null}>
      {items.length > 0 &&
        items.map((stock, index) => {
          return (
            <TradeConfirmModalItem
              itemInfo={{
                index: index,
                status,
                name: `${stock.name}`,
                quantity: stock.quantity,
              }}
              opacity={
                status === "MYSELL" && getItem && getItem.index === index
                  ? 0.1
                  : undefined
              }
              moveCardHandler={moveCardHandler}
            />
          )
        })}
    </OrderList>
  )
}

export default TradeConfirmModalList

const OrderList = styled.section`
  width: 100%;
  height: 50%;
  border: 1px solid var(--custom-gray-3);
  border-radius: 24px;
  overflow-y: scroll;

  ::-webkit-scrollbar {
    width: 12px;
  }
  ::-webkit-scrollbar-thumb {
    background: #d9d9d9;
    border-radius: 24px;
    border: 5px solid transparent;
    background-clip: padding-box;
  }
  ::-webkit-scrollbar-track {
    width: 12px;
  }
`
