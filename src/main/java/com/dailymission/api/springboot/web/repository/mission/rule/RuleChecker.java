package com.dailymission.api.springboot.web.repository.mission.rule;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RuleChecker {

    @Builder
    public RuleChecker(){

    }

//    public boolean ruleCheck(Week week){
//        if(!week.isFri().equals("N") && !week.getSun().equals("Y")){
//            return false;
//        }
//
//        if(!week.getMon().equals("N") && !week.getMon().equals("Y")){
//            return false;
//        }
//
//        if(!week.getTue().equals("N") && !week.getTue().equals("Y")){
//            return false;
//        }
//
//        if(!week.getWed().equals("N") && !week.getWed().equals("Y")){
//            return false;
//        }
//
//        if(!week.getThu().equals("N") && !week.getThu().equals("Y")){
//            return false;
//        }
//
//        if(!week.getFri().equals("N") && !week.getFri().equals("Y")){
//            return false;
//        }
//
//        if(!week.getSat().equals("N") && !week.getSat().equals("Y")){
//            return false;
//        }
//
//        return true;
//    }
}
