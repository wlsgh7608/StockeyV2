import { useRecoilState, useRecoilValue } from "recoil";
import {
  KeywordCardType,
  keywordAccordionOpenState,
  selectedLabStockState,
  labKeywordSearchInput
} from "../../stores/LaboratoryAtoms";
import { useLabKeywordSearch } from "../../hooks/useLabAccordion";
import AccordionLayout from "./AccordionLayout";

const sampleItemStock: KeywordCardType[] = [
  {
    id: 1,
    name: "keyword1",
  },
  {
    id: 2,
    name: "keyword2",
  },
  {
    id: 3,
    name: "keyword3",
  },
  {
    id: 4,
    name: "keyword4",
  },
  {
    id: 5,
    name: "keyword5",
  },
];

const KeywordAccordion = () => {
  const [openState, setOpenState] = useRecoilState(keywordAccordionOpenState);
  const selectedStock = useRecoilValue(selectedLabStockState);
  const searchInput = useRecoilValue(labKeywordSearchInput);

  // 분석 stock에 해당하는 keyword query


  // 검색 keyword query
  const {data: keywordSearch} = useLabKeywordSearch(searchInput.trim());


  // data 흐름
  // 1. 검색어 있을 경우
  // 1.1 결과 있는 경우 > 검색 결과
  // 1.2 결과 없는 경우 > 빈 배열
  // 2. 검색어 없을 경우
  // 2.1 stock 선택한 경우 > stock 관련 keyword
  // 2.2 stock 선택안한 경우 > 빈 배열

  const keywordItem = () => {
    if (searchInput.trim().length > 0 ) {
      if (keywordSearch) {
        // 1.1
        return keywordSearch
      } else {
        // 1.2
        return []
      }
    } else {
      // 2. 
      if (selectedStock) {
        // 2.1
        return sampleItemStock
      } else {
        // 2.2
        return []
      }
    }
  }

  return (
    <AccordionLayout
      type="KEYWORD"
      items={keywordItem()}
      openState={openState}
      setOpenState={setOpenState}
    />
  );
};

export default KeywordAccordion;
