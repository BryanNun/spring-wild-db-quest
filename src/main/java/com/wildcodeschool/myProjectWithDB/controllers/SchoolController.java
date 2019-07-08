package com.wildcodeschool.myProjectWithDB.controllers;

import java.util.List;

import com.wildcodeschool.myProjectWithDB.entities.School;
import com.wildcodeschool.myProjectWithDB.repositories.SchoolRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@ResponseBody
public class SchoolController {

    @GetMapping("/api/schools")
    public List<School> getSchools(@RequestParam(defaultValue = "%") String country) {
        return SchoolRepository.selectByCountry(country);
    }

    @PostMapping("/api/schools")
    @ResponseStatus(HttpStatus.CREATED)
    public School store(
        @RequestParam String name,
        @RequestParam int capacity,
        @RequestParam String country
    ) {
        int idGeneratedByInsertion = SchoolRepository.insert(name, capacity, country);
        return SchoolRepository.selectById(idGeneratedByInsertion);
    }

    @PutMapping("/api/schools/{id}")
    public School update(
        @PathVariable int id,
        @RequestParam String name,
        @RequestParam Integer capacity,
        @RequestParam String country
    ) {
        School school = SchoolRepository.selectById(id);
        SchoolRepository.update(
            id,
            name != null && name != "" ? name : school.getName(),
            capacity != null ? capacity : school.getCapacity(),
            country != null && country != "" ? country : school.getCountry()
        );
        return SchoolRepository.selectById(id);
    }

    @DeleteMapping("/api/schools/{id}")
    public void delete(@PathVariable int id) {
        SchoolRepository.delete(id);
    }

}