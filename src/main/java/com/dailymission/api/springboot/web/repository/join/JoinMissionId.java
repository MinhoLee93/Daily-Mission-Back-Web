package com.dailymission.api.springboot.web.repository.join;

import com.dailymission.api.springboot.web.repository.account.Account;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
public class JoinMissionId implements Serializable {

    private Mission mission; // JoinMission.mission

    private Account account; // JoinMission.account

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JoinMissionId that = (JoinMissionId) o;
        return Objects.equals(mission, that.mission) &&
                Objects.equals(account, that.account);
    }

    @Override
    public int hashCode(){
        return Objects.hash(mission, account);
    }
}
