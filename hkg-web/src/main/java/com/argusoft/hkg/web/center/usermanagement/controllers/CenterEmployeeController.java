/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.center.usermanagement.controllers;

import com.argusoft.hkg.web.center.transformers.CenterEmployeeTransformer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author shruti
 */
@RestController
@RequestMapping("/employee")
public class CenterEmployeeController {

    @Autowired
    private CenterEmployeeTransformer employeeTransformer;

    @RequestMapping(value = "/getprofilepicture/{login_id}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getProfileImage(@PathVariable("login_id") Long id, HttpServletRequest request) {
        String fileName = employeeTransformer.getProfileFullPath(id);
        System.out.println("File name ::: "+fileName);
        if (fileName != null && !fileName.equals("")) {
            return new FileSystemResource(fileName);
        } else {
            String retrieveAvatar = employeeTransformer.retrieveAvatar();
            if (retrieveAvatar != null) {
                return new FileSystemResource(retrieveAvatar);
            } else {
                return new FileSystemResource(request.getSession().getServletContext().getRealPath("images") + "/drmehta.gif");
            }
        }
    }

}
