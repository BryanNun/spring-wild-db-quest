package com.wildcodeschool.myProjectWithDB.controllers;

import java.sql.Date;

import com.wildcodeschool.myProjectWithDB.entities.Wizard;
import com.wildcodeschool.myProjectWithDB.repositories.WizardRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@ResponseBody
public class WizardController {

    @PostMapping("/api/wizards")
    @ResponseStatus(HttpStatus.CREATED)

    public Wizard store(
        @RequestParam String firstname,
        @RequestParam String lastname,
        @RequestParam Date birthday,
        @RequestParam(required = false, name = "birth_place") String birthPlace,
        @RequestParam(required = false) String biography,
        @RequestParam(name = "is_muggle") Boolean isMuggle
    ) {
        int idGeneratedByInsertion = WizardRepository.insert(
            firstname,
            lastname,
            birthday,
            birthPlace,
            biography,
            isMuggle
        );
        return WizardRepository.selectById(idGeneratedByInsertion);
    }
}
