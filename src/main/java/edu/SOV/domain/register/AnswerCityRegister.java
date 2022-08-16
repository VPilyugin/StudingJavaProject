package edu.SOV.domain.register;

import java.util.ArrayList;
import java.util.List;

public class AnswerCityRegister {
    List<AnswerCityRegisterItem> answerCityRegisterItemList;
    public void add(AnswerCityRegisterItem answerCityRegisterItem){
        if(answerCityRegisterItemList==null){
            answerCityRegisterItemList = new ArrayList<>();
        }
        answerCityRegisterItemList.add(answerCityRegisterItem);
    }
    public List<AnswerCityRegisterItem> getAnswerCityRegisterItemList() {
        return answerCityRegisterItemList;
    }
}
