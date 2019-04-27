package com.itberries.technopark.itberries.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@Api(value = "Контроллер для работы с ВК")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @ApiOperation(value = "Получает параметры пользователя от ВК")
    @GetMapping(value = "/")
    @ResponseBody
    String authenticateUser(HttpServletRequest request, HttpServletResponse httpServletResponse, HttpSession httpSession) {
        request.getRequestURL();
        logger.info(String.format("Query String:%s", request.getQueryString()));

        //return " <meta http-equiv=\"refresh\" content=\"0; url=https://itberries-frontend.now.sh/\" />";
//        return "<!DOCTYPE html>\n" +
//                "<html lang=\"en\">\n" +
//                "\t<head>\n" +
//                "\t\t\t<meta charset=\"utf-8\">\n" +
//                "\t\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no, user-scalable=no, viewport-fit=cover\">\n" +
//                "\t\t\t<meta name=\"theme-color\" content=\"#000000\">\n" +
//                "\t\t\t<title>IT Berries frontend</title>\n" +
//                "\t</head>\n" +
//                "\t<body>\n" +
//                "\t\t\t<div id=\"root\"></div>\n" +
//                "\t</body>\n" +
//                "</html>\n";
        httpServletResponse.setHeader("Location", "https://itberries-explority.now.sh");
        httpServletResponse.setStatus(302);
        return "";
    }
}
