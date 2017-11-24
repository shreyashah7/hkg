/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.rapcalc.restcontroller;

/**
 *
 * @author shruti
 */
public interface WebsocketInterface {
    public void evalGetMasters(String data);

    public void evalGetDiscountDetails(String data);

    public void evalGetBasePrice(String data);

    public void evalGetFourCdiscount(String data);
}
