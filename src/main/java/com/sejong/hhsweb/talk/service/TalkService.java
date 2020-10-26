package com.sejong.hhsweb.talk.service;

import java.util.ArrayList;

import com.sejong.hhsweb.model.Talk;
import com.sejong.hhsweb.model.TalkSpace;

public interface TalkService {

	ArrayList<TalkSpace> selectTalkList(String userId);

	ArrayList<Talk> selectTalksList(int tsnum);


}
