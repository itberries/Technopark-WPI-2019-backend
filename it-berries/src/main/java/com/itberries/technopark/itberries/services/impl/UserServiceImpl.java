package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.*;
import com.itberries.technopark.itberries.models.*;
import com.itberries.technopark.itberries.services.ISectionService;
import com.itberries.technopark.itberries.services.IStepService;
import com.itberries.technopark.itberries.services.ISubsectionService;
import com.itberries.technopark.itberries.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    final IUserDAO userDAO;
    final IUserStateDAO userStateDAO;
    final ISectionDAO sectionDAO;
    final ISubsectionDAO subsectionDAO;
    final IStepDAO stepDAO;
    final ISectionService iSectionService;
    final ISubsectionService iSubsectionService;
    final IStepService iStepService;

    @Autowired
    public UserServiceImpl(IUserDAO userDAO, IUserStateDAO userStateDAO, ISectionDAO sectionDAO, ISubsectionDAO subsectionDAO, IStepDAO stepDAO, ISectionService iSectionService, ISubsectionService iSubsectionService, IStepService iStepService) {
        this.userDAO = userDAO;
        this.userStateDAO = userStateDAO;
        this.sectionDAO = sectionDAO;
        this.subsectionDAO = subsectionDAO;
        this.stepDAO = stepDAO;
        this.iSectionService = iSectionService;
        this.iSubsectionService = iSubsectionService;
        this.iStepService = iStepService;
    }

    @Override
    public User getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    @Override
    public Integer setUser(Long id) {
        return userDAO.setUser(id);
    }

    @Override
    public boolean setCurrentUserSectionCompleted(Long userId, Long sectionId) {
        UserState userState = null;
        Section section = null;
        Subsection subsection = null;
        List<Step> steps = null;
        Step step = null;

        userState = userStateDAO.getUserState(userId);
        section = sectionDAO.getSectionById(sectionId);

        if ((userState != null) && (isCurrentSectionCurrentOrPrevious(sectionId, userState.getSectionId()) > 0)) {
            return true;
        } else {
            if ((userState != null) && (section != null) && (isCurrentSectionCurrentOrPrevious(sectionId, userState.getSectionId()) == 0) && (section.getChildId() != 0)) {
                userState.setSectionId(Long.parseLong(section.getChildId().toString()));

                subsection = subsectionDAO.getSubsectionBySectionIdAndParentId(userState.getSectionId(),Long.parseLong("0"));
                if (subsection != null) {

                    steps = stepDAO.getStepsBySectionId(Integer.parseInt(subsection.getId().toString()));
                    steps.sort(Comparator.comparingInt(Step::getParentId));
                    step = steps.get(0);
                    if ((step != null) && (step.getParentId() == 0)) {
                        userState.setUserId(userId);
                        userState.setSubsectionId(subsection.getId());
                        userState.setStepId(Long.parseLong(step.getId().toString()));

                        return userStateDAO.setUserState(userId,userState);
                    }
                }
            } else {
                if ((section != null) && (isCurrentSectionCurrentOrPrevious(sectionId, userState.getSectionId()) == 0) && (section.getChildId() == 0)) {
                    userState.setUserId(userId);
                    userState.setHasPassedApplication(true);

                    return userStateDAO.setUserState(userId,userState);
                }
            }
        }
        /*if ((userState != null) &&(userState.getSectionId() > sectionId)) {
            return true;
        } else {
            // переключаемся по секциям, если у них есть дети и вообще если есть инфа по state пользователя
            if ((userState != null) && (userState.getSectionId() == sectionId) && (section != null) && (section.getChildId() != 0)) {
                userState.setSectionId(Long.parseLong(section.getChildId().toString()));

                subsection = subsectionDAO.getSubsectionBySectionIdAndParentId(userState.getSectionId(),Long.parseLong("0"));
                if (subsection != null) {

                    steps = stepDAO.getStepsBySectionId(Integer.parseInt(subsection.getId().toString()));
                    steps.sort(Comparator.comparingInt(Step::getParentId));
                    step = steps.get(0);
                    if ((step != null) && (step.getParentId() == 0)) {
                        userState.setUserId(userId);
                        userState.setSubsectionId(subsection.getId());
                        userState.setStepId(Long.parseLong(step.getId().toString()));

                        return userStateDAO.setUserState(userId,userState);
                    }
                }
            } else {
                if ((section != null) && (section.getChildId() == 0)) {
                    userState.setUserId(userId);
                    userState.setHasPassedApplication(true);

                    return userStateDAO.setUserState(userId,userState);
                }
            }
        }*/
        return false;
    }

    @Override
    public boolean setCurrentUserSubsectionCompleted(Long userId, Long sectionId, Long subsectionId) {
        UserState userState = null;
        Section section = null;
        Subsection subsection = null;
        List<Step> steps = null;
        Step step = null;

        userState = userStateDAO.getUserState(userId);

        // проверка, есть ли state пользователя
        if (userState != null) {
            // проверяем, если текущая секция меньше  вернуть ок без обновления
            if (isCurrentSectionCurrentOrPrevious(sectionId,userState.getSectionId()) > 0) {
                return true;
            } else {
                section = sectionDAO.getSectionById(sectionId);
                // проверили, что в нужной секции
                if ((section != null) && (isCurrentSectionCurrentOrPrevious(sectionId,userState.getSectionId()) == 0) ) {
                    // если подсекция меньше - вернуть ок без обновления
                    if (isCurrentSubsectionCurrentOrPrevious(subsectionId,userState.getSubsectionId(), userState.getSectionId()) > 0) {
                        return true;
                    } else {
                        subsection = subsectionDAO.getSubsectionById(subsectionId);
                        // проверили, что в нужной подсекции
                        if ((subsection != null) && (isCurrentSubsectionCurrentOrPrevious(subsectionId,userState.getSubsectionId(), userState.getSectionId()) == 0)) {
                            // переключаемся по подсекциям или переход на новую секцию
                            if (subsection.getChildId() != 0) {
                                steps = stepDAO.getStepsBySectionId(Integer.parseInt(subsection.getChildId().toString()));
                                steps.sort(Comparator.comparingInt(Step::getParentId));
                                step = steps.get(0);
                                if ((step != null) && (step.getParentId() == 0)) {
                                    userState.setSubsectionId(Long.parseLong(subsection.getChildId().toString()));
                                    userState.setStepId(Long.parseLong(step.getId().toString()));

                                    return userStateDAO.setUserState(userId,userState);
                                }
                            } else {
                                return this.setCurrentUserSectionCompleted(userId,sectionId);
                            }
                        }
                    }
                }
            }
        }

        /*// проверка, есть ли state пользователя
        if (userState != null) {
            // проверяем, если текущая секция меньше  вернуть ок без обновления
            if (userState.getSectionId() > sectionId) {
                return true;
            } else {
                section = sectionDAO.getSectionById(sectionId);
                // проверили, что в нужной секции
                if ((section != null) && (userState.getSectionId() == sectionId)) {

                    // если подсекция меньше - вернуть ок без обновления
                    if (userState.getSubsectionId() > subsectionId) {
                        return true;
                    } else {
                        subsection = subsectionDAO.getSubsectionById(subsectionId);
                        // проверили, что в нужной подсекции
                        if ((subsection != null) && (userState.getSubsectionId() == subsectionId)) {

                            // переключаемся по подсекциям или переход на новую секцию
                            if (subsection.getChildId() != 0) {
                                steps = stepDAO.getStepsBySectionId(Integer.parseInt(subsection.getChildId().toString()));
                                steps.sort(Comparator.comparingInt(Step::getParentId));
                                step = steps.get(0);
                                if ((step != null) && (step.getParentId() == 0)) {
                                    userState.setSubsectionId(Long.parseLong(subsection.getChildId().toString()));
                                    userState.setStepId(Long.parseLong(step.getId().toString()));

                                    return userStateDAO.setUserState(userId,userState);
                                }
                            } else {
                                return this.setCurrentUserSectionCompleted(userId,sectionId);
                            }
                        }
                    }
                }
            }
        }*/
        return false;
    }

    @Override
    public boolean setCurrentUserStepCompleted(Long userId, Long sectionId, Long subsectionId, Long stepId) {
        UserState userState = null;
        Section section = null;
        Subsection subsection = null;
        Step step = null;

        userState = userStateDAO.getUserState(userId);

        if (userState != null) {
            // проверяем, если текущая секция меньше  вернуть ок без обновления
            if (isCurrentSectionCurrentOrPrevious(sectionId,userState.getSectionId()) > 0) {
                return true;
            } else {
                section = sectionDAO.getSectionById(sectionId);
                // проверили, что в нужной секции
                if ((section != null) && (isCurrentSectionCurrentOrPrevious(sectionId,userState.getSectionId()) == 0)) {
                    // проверяем, если текущая подсекция меньше  вернуть ок без обновления
                    if (isCurrentSubsectionCurrentOrPrevious(subsectionId,userState.getSubsectionId(), userState.getSectionId()) > 0) {
                        return true;
                    } else {
                        subsection = subsectionDAO.getSubsectionById(subsectionId);
                        // проверили, что в нужной подсекции
                        if ((subsection != null) && (isCurrentSubsectionCurrentOrPrevious(subsectionId,userState.getSubsectionId(), userState.getSectionId()) == 0)) {
                            // проверяем, если текущая стэп меньше  вернуть ок без обновления
                            if (isCurrentStepCurrentOrPrevious(stepId,userState.getStepId(),userState.getSubsectionId()) > 0) {
                                return true;
                            } else {
                                step = stepDAO.getStepById(Integer.parseInt(stepId.toString()));
                                if ((step != null) && (isCurrentStepCurrentOrPrevious(stepId,userState.getStepId(),userState.getSubsectionId()) == 0)) {
                                    if (step.getChildId() != 0) {
                                        userState.setStepId(Long.parseLong(step.getChildId().toString()));
                                        return userStateDAO.setUserState(userId,userState);
                                    } else {
                                        return this.setCurrentUserSubsectionCompleted(userId,sectionId,subsectionId);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        /*// проверка, есть ли state пользователя
        if (userState != null) {
            // проверяем, если текущая секция меньше  вернуть ок без обновления
            if (userState.getSectionId() > sectionId) {
                return true;
            } else {
                section = sectionDAO.getSectionById(sectionId);
                // проверили, что в нужной секции
                if ((section != null) && (userState.getSectionId() == sectionId)) {

                    // проверяем, если текущая подсекция меньше  вернуть ок без обновления
                    if (userState.getSubsectionId() > subsectionId) {
                        return true;
                    } else {
                        subsection = subsectionDAO.getSubsectionById(subsectionId);
                        // проверили, что в нужной подсекции
                        if ((subsection != null) && (userState.getSubsectionId() == subsectionId)) {

                            // проверяем, если текущая стэп меньше  вернуть ок без обновления
                            if (userState.getStepId() > stepId) {
                                return true;
                            } else {
                                step = stepDAO.getStepById(Integer.parseInt(stepId.toString()));
                                if ((step != null) && (userState.getStepId() == stepId)) {
                                    if (step.getChildId() != 0) {
                                        userState.setStepId(Long.parseLong(step.getChildId().toString()));
                                        return userStateDAO.setUserState(userId,userState);
                                    } else {
                                        return this.setCurrentUserSubsectionCompleted(userId,sectionId,subsectionId);
                                    }
                                }
                            }
                        }
                    }


                }
            }

        }*/

        return false;
    }

    @Override
    public UserState getCurrentUserState(Long id) {
        return userStateDAO.getUserState(id);
    }

    @Override
    public boolean resetCurrentUserState(Long id) {
        UserState us = new UserState();
        Section section = null;
        Subsection subsection = null;
        List<Step> steps = null;
        Step step = null;

        section = sectionDAO.getSectionByParentId(Long.parseLong("0"));
        if (section != null) {
            subsection = subsectionDAO.getSubsectionBySectionIdAndParentId(section.getId(),Long.parseLong("0"));
            if (subsection != null) {

                steps = stepDAO.getStepsBySectionId(Integer.parseInt(subsection.getId().toString()));
                steps.sort(Comparator.comparingInt(Step::getParentId));
                step = steps.get(0);
                if ((step != null) && (step.getParentId() == 0)) {
                    us.setUserId(id);
                    us.setSectionId(section.getId());
                    us.setSubsectionId(subsection.getId());
                    us.setStepId(Long.parseLong(step.getId().toString()));
                    us.setHasPassedApplication(false);

                    userDAO.setScore(0,id);

                    return userStateDAO.setUserState(id,us);
                }
            }
        }
        return false;
    }

    @Override
    public void initUserState(Long userId) {
        userStateDAO.initUserState(userId);
    }

    @Override
    public Integer isCurrentSectionCurrentOrPrevious(Long idToCheck, Long currentStateId) {
        List<Long> orderedSectionIds = iSectionService.getOrderedSectionsIdentifiers();
        Integer positionToCheck = orderedSectionIds.size() + 1;
        Integer currentPosition = orderedSectionIds.size() + 1;

        currentPosition = orderedSectionIds.indexOf(currentStateId);
        positionToCheck = orderedSectionIds.indexOf(idToCheck);
        if (positionToCheck < 0) {
            positionToCheck = orderedSectionIds.size() + 1;
        }
        return (currentPosition - positionToCheck);
    }

    @Override
    public Integer isCurrentSubsectionCurrentOrPrevious(Long idToCheck, Long currentStateId, Long sectionId) {
        List<Long> orderedSubsectionIds = iSubsectionService.getOrderedSubsectionsIdentifiers(sectionId);
        Integer positionToCheck = orderedSubsectionIds.size() + 1;
        Integer currentPosition = orderedSubsectionIds.size() + 1;

        currentPosition = orderedSubsectionIds.indexOf(currentStateId);
        positionToCheck = orderedSubsectionIds.indexOf(idToCheck);
        if (positionToCheck < 0) {
            positionToCheck = orderedSubsectionIds.size() + 1;
        }
        return (currentPosition - positionToCheck);
    }

    @Override
    public Integer isCurrentStepCurrentOrPrevious(Long idToCheck, Long currentStateId, Long subsectionId) {
        List<Long> orderedStepIds = iStepService.getOrderedStepsIdentifiers(subsectionId);
        Integer positionToCheck = orderedStepIds.size() + 1;
        Integer currentPosition = orderedStepIds.size() + 1;

        currentPosition = orderedStepIds.indexOf(currentStateId);
        positionToCheck = orderedStepIds.indexOf(idToCheck);
        if (positionToCheck < 0) {
            positionToCheck = orderedStepIds.size() + 1;
        }
        return (currentPosition - positionToCheck);
    }

}
