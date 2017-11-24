package com.argusoft.hkg.web.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.argusoft.hkg.web.common.databeans.MessageDataBean;
import java.util.Map;

public class ResponseEntity<T> {

    private T data;
    private List<MessageDataBean> messages;
    private boolean processResponse;

    private ResponseEntity() {
        super();
    }

    /**
     *
     * @param data Data to be passed to client.
     * @param responseCode code that indicates the nature of response-
     * informative, error, success, failure etc. Use this if only single
     * messages is to be displayed
     * @param message single message to be displayed. Ignored if blank or null.
     * Use this if only single messages is to be displayed
     * @param translateValueMap map of key and value of msg
     * @param messages Any extra messages to be displayed.
     */
    public ResponseEntity(T data, ResponseCode responseCode, String message, Map translateValueMap, List<MessageDataBean> messages) {
        this(data, responseCode, message, translateValueMap, messages, true);
    }

    /**
     *
     * @param data Data to be passed to client.
     * @param responseCode code that indicates the nature of response-
     * informative, error, success, failure etc. Use this if only single
     * messages is to be displayed
     * @param message single message to be displayed. Ignored if blank or null.
     * Use this if only single messages is to be displayed
     * @param messages Any extra messages to be displayed.
     */
    public ResponseEntity(T data, ResponseCode responseCode, String message, List<MessageDataBean> messages) {
        this(data, responseCode, message, null, messages, true);
    }

    /**
     * This method has additional param processResponse. Use it only if you
     * really need it.
     *
     * @param data Data to be passed to client.
     * @param responseCode code that indicates the nature of response-
     * informative, error, success, failure etc. Use this if only single
     * messages is to be displayed
     * @param message single message to be displayed. Ignored if blank or null.
     * Use this if only single messages is to be displayed
     * @param translateValueMap map of key and value of msg
     * @param messages Any extra messages to be displayed.
     * @param processResponse weather to process response or not. If set to
     * false, response will be bypassed by the server. i.e. client interceptor
     * will not render any messages, and original response entity will be
     * received inside JS controller.
     */
    public ResponseEntity(T data, ResponseCode responseCode, String message, Map translateValueMap, List<MessageDataBean> messages, boolean processResponse) {
        this();
        this.data = data;
        if (!StringUtils.isEmpty(message) && responseCode != null) {
            if (messages == null) {
                messages = new ArrayList<>();
            }
            messages.add(new MessageDataBean(null, responseCode, message, translateValueMap));
        }
        this.messages = messages;
        this.processResponse = processResponse;
    }

    /**
     * This method has additional param processResponse. Use it only if you
     * really need it.
     *
     * @param data Data to be passed to client.
     * @param responseCode code that indicates the nature of response-
     * informative, error, success, failure etc. Use this if only single
     * messages is to be displayed
     * @param message single message to be displayed. Ignored if blank or null.
     * Use this if only single messages is to be displayed
     * @param messages Any extra messages to be displayed.
     * @param processResponse weather to process response or not. If set to
     * false, response will be bypassed by the server. i.e. client interceptor
     * will not render any messages, and original response entity will be
     * received inside JS controller.
     */
    public ResponseEntity(T data, ResponseCode responseCode, String message, List<MessageDataBean> messages, boolean processResponse) {
        this();
        this.data = data;
        if (!StringUtils.isEmpty(message) && responseCode != null) {
            if (messages == null) {
                messages = new ArrayList<>();
            }
            messages.add(new MessageDataBean(null, responseCode, message, null));
        }
        this.messages = messages;
        this.processResponse = processResponse;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }


    public List<MessageDataBean> getMessages() {
        return messages;
    }


    public void setMessages(List<MessageDataBean> messages) {
        this.messages = messages;
    }

    public boolean isPorcessResponse() {
        return processResponse;
    }

    public void setPorcessResponse(boolean porcessResponse) {
        this.processResponse = porcessResponse;
    }

	
	
}
