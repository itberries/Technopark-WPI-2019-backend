package com.itberries.technopark.itberries.dao;

import com.itberries.technopark.itberries.models.UserState;

public interface IUserStateDAO {
    UserState getUserState(Long userId);
    boolean setUserState(Long userId, UserState userState);
    boolean setCurrentUserSection(Long userId, Integer destSectionId);
    void initUserState(Long userId);
    UserState getCurrentStateByStepId(Long stepId);
}
