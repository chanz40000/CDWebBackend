package com.example.cdwebbackend.api;


import com.example.cdwebbackend.api.output.NewOutput;
import com.example.cdwebbackend.service.impl.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController

public class NewApi {
    @Autowired
    private NewService newService;

    @GetMapping("/new")
    public NewOutput showNew(@RequestParam("page")int page,
                             @RequestParam("limit")int limit){
        NewOutput result = new NewOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page-1, limit);
        result.setListResult(newService.findAll(pageable));
        result.setTotalPage((int)Math.ceil((double) (newService.totalItem())/limit));
        return result;
    }

//    @PostMapping("/new")
//public NewDTO createNew(@RequestBody NewDTO model){
//    newService.save(model);
//    return model;
//}
//
//@PutMapping(value = "/new/{id}")
//    public NewDTO updateNew(@RequestBody NewDTO model, @PathVariable ("id")long id){
//    model.setId(id);
//
//    return newService.save(model);
//}
//@DeleteMapping(value = "/new")
//    public void deleteNew(@RequestBody long[]ids){
//
//    newService.delete(ids);
//}
}
