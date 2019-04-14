package com.itberries.technopark.itberries.services;


import com.itberries.technopark.itberries.models.User;
import com.itberries.technopark.itberries.models.UserState;

public interface IUserService {
    User getUserById(Long id);
    Integer setUser(Long id);

    /**
     * Производит перевод пользователя на другую секцию -
     * секцию-ребенка
     *
     * @param userId
     * @param sectionId
     * @return признак_успеха_обновления
     */
    boolean setCurrentUserSectionCompleted(Long userId, Long sectionId);

    /**
     * Производит перевод пользователя на другую подсекцию -
     * подсекцию-ребенка
     *
     * @param userId
     * @param sectionId
     * @param subsectionId
     * @return признак_успеха_обновления
     */
    boolean setCurrentUserSubsectionCompleted(Long userId,Long sectionId,Long subsectionId);

    /**
     * Производит перевод пользователя на другой шаг подсекции -
     * шаг-ребенка
     *
     * @param userId
     * @param sectionId
     * @param subsectionId
     * @param stepId
     * @return признак_успеха_обновления
     */
    boolean setCurrentUserStepCompleted(Long userId,Long sectionId,Long subsectionId, Long stepId);

    /**
     * Получает текущее состояние пользователя (секция, подсекция, шаг)
     * @param userId - уникальный идентификатор пользователя
     * @return состояние пользователя
     */
    UserState getCurrentUserState(Long userId);

    /**
     * Сбрасывает текущее состояние пользователя (секция, подсекция, шаг)
     * @param id
     * @return признак сброса (успех/неуспех)
     */
    boolean resetCurrentUserState(Long id);

    /**
     * Устанавливает состояние нового пользователя в начальное
     * @param userId уникальный идентификатор пользователя
     */
    void initUserState(Long userId);
}
