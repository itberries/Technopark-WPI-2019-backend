package com.itberries.technopark.itberries.controllers;

import com.itberries.technopark.itberries.models.*;
import com.itberries.technopark.itberries.responses.UserNotAuthorizedException;
import com.itberries.technopark.itberries.responses.models.StepListResponse;
import com.itberries.technopark.itberries.services.IStepService;
import com.itberries.technopark.itberries.services.ISubsectionService;
import com.itberries.technopark.itberries.services.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/subsections/")
public class SubsectionController {

    private final ISubsectionService iSubsectionService;
    private final IStepService iStepService;
    private final IUserService iUserService;

    @Autowired
    private SubsectionController(ISubsectionService iSubsectionService, IStepService iStepService, IUserService iUserService) {
        this.iSubsectionService = iSubsectionService;
        this.iStepService = iStepService;
        this.iUserService = iUserService;
    }


    @ApiOperation(value = "Получить список подсекций для заданной секции")
    @GetMapping(value = "/{name}/")
    @ResponseBody
    List<Subsection> getSubsectionsBySectionName(@PathVariable(name = "name") String name) {
        List<Long> sectionIds = iSubsectionService.getOrderedSubsectionsIdentifiers(new Long(1));
        return iSubsectionService.getSubsectionsByName(name);
    }

    @GetMapping(value = "/{name}/{id}/")
    @ResponseBody
    List<Theory> getTheoryPagesForSubsectionBySubsectionId(@PathVariable(name = "name") String name,
                                                           @PathVariable(name = "id") Long id) {
        return iSubsectionService.getTheoryPagesForSubsectionBySubsectionId(name, id);
    }

    @GetMapping(value = "/{subsection_id}/steps/")
    @ApiOperation(value = "Получить шаги подсекции с указанием шага,  на котором остановился пользователь")
    @ResponseBody
    StepListResponse getStepsBySubsectionId(@PathVariable(name = "subsection_id") Integer subsectionId,
                                            HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UserNotAuthorizedException();
        }

        List<Long> st = iStepService.getOrderedStepsIdentifiers(Long.parseLong(subsectionId.toString()));

        List<Step> stepsBySectionId = iStepService.getStepsBySectionId(subsectionId);

        UserState userState = iUserService.getCurrentUserState(user.getId());

        Step currentStep = iStepService
                .getCurrentStepByUserIdAndSubsectionId(user.getId(), Integer.parseInt(userState.getSubsectionId().toString()));

        return new StepListResponse(stepsBySectionId, currentStep);
    }


}
