package com.argusoft.hkg.web.base.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.argusoft.hkg.web.base.PrimaryKey;
import com.argusoft.hkg.web.base.ResponseCode;
import com.argusoft.hkg.web.base.ResponseEntity;
import com.argusoft.hkg.web.common.databeans.MessageDataBean;
import java.util.Map;

/**
 *
 * @author Tejas
 */
public abstract class BaseController<T, PK> {

    @RequestMapping(value = "retrieve", method = RequestMethod.GET)
    public abstract ResponseEntity<List<T>> retrieveAll();

    @RequestMapping(value = "retrieve", method = RequestMethod.POST)
    public abstract ResponseEntity<T> retrieveById(@RequestBody PrimaryKey<PK> primaryKey);

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public abstract ResponseEntity<PrimaryKey<PK>> update(@RequestBody T t);

    @RequestMapping(value = "create", method = RequestMethod.PUT)
    public abstract ResponseEntity<PrimaryKey<PK>> create(@RequestBody T t);

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public abstract ResponseEntity<T> deleteById(@RequestBody PrimaryKey<PK> primaryKey);

    @RequestMapping(value = "retrieveprerequisite", method = RequestMethod.GET)
    public abstract ResponseEntity<Map<String, Object>> retrievePrerequisite();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<T> processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();
        List<MessageDataBean> messages = new ArrayList<MessageDataBean>();
        if (!CollectionUtils.isEmpty(errors)) {
            long i = 0;
            for (FieldError fieldError : errors) {
                //TODO: Add internationalization to validation message
                messages.add(new MessageDataBean(i, ResponseCode.FAILURE, fieldError.getDefaultMessage()));
                i++;
            }
        }
        return new ResponseEntity<T>(null, ResponseCode.FAILURE, "", messages);
    }
}
