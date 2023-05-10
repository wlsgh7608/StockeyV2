import { useRecoilValue } from "recoil";
import {
  stockAccordionOpenState,
  keywordAccordionOpenState,
} from "../stores/LaboratoryAtoms";

import StockAccordion from "../components/LabPage/StockAccordion";
import KeywordAccordion from "../components/LabPage/KeywordAccordion";
import TargetListSection from "../components/LabPage/TargetListSection";
import ResultBoard from "../components/LabPage/ResultBoard";

import styled from "styled-components";

const LabPage = () => {
  const stockOpenState = useRecoilValue(stockAccordionOpenState);
  const keywordOpenState = useRecoilValue(keywordAccordionOpenState);

  // accordion 둘다 접혀져있는 경우, result panel 큰 버전 랜더링
  return (
    <PageWrapper>
      <ColumnWrapper width={"25%"}>
        <StockAccordion />
        <KeywordAccordion />
      </ColumnWrapper>

      <ColumnWrapper width={"65%"}>
        <TargetListSection />
      </ColumnWrapper>

      <AbsoluteWrapper resultSize={stockOpenState || keywordOpenState}>
        <ResultBoard resultSize={stockOpenState || keywordOpenState}/>
      </AbsoluteWrapper>
    </PageWrapper>
  );
};

export default LabPage;

const PageWrapper = styled.div`
  margin: 5% 10%;
  display: flex;
  justify-content: space-between;
  positon: relative;
`;

const ColumnWrapper = styled.div<{width: string}>`
  display: flex;
  flex-direction: column;

  width: ${props => props.width};
  gap: 45px;
`;

const AbsoluteWrapper = styled.div<{ resultSize: boolean }>`
  position: absolute;
  padding: 0 0 5% 0;
  right: 10%;
  top: ${(props) => (props.resultSize ? "415px" : "460px")};
  width: ${(props) => (props.resultSize ? "52%" : "80%")};

  -webkit-transition: width 1s, top 1s;
  transition: width 1s, top 1s;
`;