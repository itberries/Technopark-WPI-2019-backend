package com.itberries.technopark.itberries.controllers;

import com.itberries.technopark.itberries.models.Section;
import com.itberries.technopark.itberries.models.Subsection;
import com.itberries.technopark.itberries.models.Theory;
import com.itberries.technopark.itberries.responses.SubsectionDoesNotExistInThisSection;
import com.itberries.technopark.itberries.responses.ThereIsNoSectionsException;
import com.itberries.technopark.itberries.services.ISectionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sections/")
public class SectionController {
    final ISectionService iSectionService;

    @Autowired
    public SectionController(ISectionService iSectionService) {
        this.iSectionService = iSectionService;
    }

    @GetMapping(value = "/")
    @ResponseBody
    @ApiOperation(value = "Получить все секции с подсекциями")
    List<Section> getSections() {
        List<Long> sectionIds = iSectionService.getOrderedSectionsIdentifiers();
        return iSectionService.getSections();
    }

    @GetMapping(value = "/{id}/subsections/")
    @ResponseBody
    List<Subsection> getSubsectionsBySectionId(@PathVariable(name = "id") Long id) {

        return iSectionService.getSubsectionsBySectionId(id);
    }

    @GetMapping(value = "/{id}/subsections/{id_subsection}/")
    @ResponseBody
    List<Theory> getTheoryBySectionIdAndSubsectionId(@PathVariable(name = "id") Long id,
                                                     @PathVariable(name = "id_subsection") Long id_subsection) {
        // need to check section id first
        if (iSectionService.doesSectionExist(id)) {
            // need to check match of subsection its section
            if (iSectionService.doesSubsectionMatchSection(id_subsection, id)) {
                return iSectionService.getTheoryBySectionIdAndSubsectionId(id_subsection);
            } else {
                throw new SubsectionDoesNotExistInThisSection();
            }

        } else {
            throw new ThereIsNoSectionsException();
        }

    }
}
