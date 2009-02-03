package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.incentive.Incentive;
import com.dneero.incentivetwit.Incentivetwit;

/**
 * User: Joe Reger Jr
 * Date: Aug 1, 2008
 * Time: 2:05:19 PM
 */
public class AccountAwardsTwitListitem {

    private Incentivetwitaward incentivetwitaward;
    private Twitanswer twitanswer;
    private Twitask twitask;
    private Twitaskincentive Twitaskincentive;
    private Incentivetwit incentivetwit;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }

    public Incentivetwitaward getIncentivetwitaward() {
        return incentivetwitaward;
    }

    public void setIncentivetwitaward(Incentivetwitaward incentivetwitaward) {
        this.incentivetwitaward=incentivetwitaward;
    }

    public Twitanswer getTwitanswer() {
        return twitanswer;
    }

    public void setTwitanswer(Twitanswer twitanswer) {
        this.twitanswer=twitanswer;
    }

    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }

    public Twitaskincentive getTwitaskincentive() {
        return Twitaskincentive;
    }

    public void setTwitaskincentive(Twitaskincentive twitaskincentive) {
        Twitaskincentive=twitaskincentive;
    }

    public Incentivetwit getIncentivetwit() {
        return incentivetwit;
    }

    public void setIncentivetwit(Incentivetwit incentivetwit) {
        this.incentivetwit=incentivetwit;
    }
}