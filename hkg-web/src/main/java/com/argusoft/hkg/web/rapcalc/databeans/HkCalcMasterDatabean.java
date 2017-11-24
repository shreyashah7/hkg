/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.web.rapcalc.databeans;

/**
 *
 * @author shruti
 */
public class HkCalcMasterDatabean {
 
        private String NAME;
        private int SEQUENCE;
        private String CUSTOMFIELD;

        /**
         * @return the NAME
         */
        public String getName() {
            return NAME;
        }

        /**
         * @param name the NAME to set
         */
        public void setName(String name) {
            this.NAME = name;
        }

        /**
         * @return the SEQUENCE
         */
        public int getSequence() {
            return SEQUENCE;
        }

        /**
         * @param sequence the SEQUENCE to set
         */
        public void setSequence(int sequence) {
            this.SEQUENCE = sequence;
        }

        /**
         * @return the CUSTOMFIELD
         */
        public String getCustomfield() {
            return CUSTOMFIELD;
        }

        /**
         * @param customfield the CUSTOMFIELD to set
         */
        public void setCustomfield(String customfield) {
            this.CUSTOMFIELD = customfield;
        }
   
}
