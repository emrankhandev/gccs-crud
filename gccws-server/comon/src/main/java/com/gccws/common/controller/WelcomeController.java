package com.gccws.common.controller;
import com.gccws.common.model.WelcomeModel;
import com.gccws.common.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


import lombok.AllArgsConstructor;

import javax.validation.Valid;

import static com.gccws.common.utils.CommonConstants.INT_V_P;

/**
 * @Author    Md. Chabed alam
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */


@AllArgsConstructor
@RestController
@RequestMapping("/")
public class WelcomeController {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(WelcomeController.class);

    private CommonUtils commonUtils;

    @GetMapping
    public  String getAll() {
	    return " API UP & Running.................";
    }

    @GetMapping("/{number:" + INT_V_P +"}")
    public  String getByPath(@PathVariable("number") Integer number) {
        return ""+ number;
    }

    @PostMapping
    public String save(@Valid @RequestBody WelcomeModel model){
        // Sanitize HTML
        model.setDescription(commonUtils.sanitizeHtmlCustom(model.getDescription()));
        // Sanitize HTML
        return "Save success " + model.toString();
    }


}

