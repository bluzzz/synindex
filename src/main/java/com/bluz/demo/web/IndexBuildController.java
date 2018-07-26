package com.bluz.demo.web;

import com.bluz.demo.service.IndexBuildService;
import com.bluz.demo.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/indexbuild")
public class IndexBuildController {
    @Autowired
    private IndexBuildService indexBuildService;

    @GetMapping("/{index}")
    @ResponseBody
    public Response build(@PathVariable("index") String index){
        Response.Builder builder=Response.Builder.newBuilder();
        try {
             indexBuildService.build(index);
            return builder.success().msg("success").build();
        } catch (Exception e) {
            e.printStackTrace();
            return builder.error().msg(e.getMessage()).build();
        }
    }
}
