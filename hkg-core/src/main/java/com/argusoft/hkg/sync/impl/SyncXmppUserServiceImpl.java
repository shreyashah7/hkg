/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.hkg.sync.impl;

import com.argusoft.generic.database.common.CommonDAO;
import com.argusoft.generic.database.search.SearchFactory;
import com.argusoft.hkg.sql.sync.model.SyncXmppUser;
import com.argusoft.hkg.sync.SyncXmppUserService;
import com.googlecode.genericdao.search.Search;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class stores the credentials of the user for xmpp server. That will be
 * one user per franchise.
 *
 * @author shruti
 */
@Service
@Transactional
public class SyncXmppUserServiceImpl implements SyncXmppUserService {

    @Autowired
    CommonDAO commonDAO;

    private static class Fields {

        static final String USER_NAME = "userName";
        static final String FRACHISE = "franchise";
        static final String USER_CREATED_IN_TIGASE = "userCreatedInTigase";
        static final String IS_ACTIVE = "is_active";
        static final String IS_ARCHIVE = "is_archive";
    }

    @Override
    public Boolean createUser(SyncXmppUser hkXmppUser) {
        return commonDAO.save(hkXmppUser);
    }

    @Override
    public void updateUser(SyncXmppUser user) {
        commonDAO.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        SyncXmppUser user = commonDAO.find(SyncXmppUser.class, id);
        user.setIsArchive(false);
        commonDAO.save(user);
    }

    @Override
    public SyncXmppUser getUserbyUserName(String userName) {
        Search search = SearchFactory.getSearch(SyncXmppUser.class);
        search.addFilterEqual(Fields.USER_NAME, userName);
        return (SyncXmppUser) commonDAO.search(search);
    }

    @Override
    public SyncXmppUser getUserbyFranchise(Long franchise) {

        Search search = SearchFactory.getSearch(SyncXmppUser.class);
        search.addFilterEqual(Fields.FRACHISE, franchise);
        List<SyncXmppUser> listXmppUser = commonDAO.search(search);

        return (listXmppUser != null && listXmppUser.size() > 0) ? listXmppUser.get(0) : null;
    }

    @Override
    public String resetPassword(String userId, String newPassword) {
        return null;
    }

    @Override
    public void activateUser(String userName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deActiveUser(String userName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SyncXmppUser> retrieveUserByUserCreatedInTigase(Boolean userCreatedInTigase) {
        Search search = SearchFactory.getSearch(SyncXmppUser.class);
        search.addFilterEqual(Fields.USER_CREATED_IN_TIGASE, userCreatedInTigase);
        List<SyncXmppUser> listXmppUser = commonDAO.search(search);
        return listXmppUser;
    }

    @Override
    public List<SyncXmppUser> retrieveAllActiveUsers() {
        Search search = SearchFactory.getSearch(SyncXmppUser.class);
//        search.addFilterEqual(Fields.IS_ACTIVE, true);
//        search.addFilterEqual(Fields.IS_ARCHIVE, Boolean.FALSE);
        List<SyncXmppUser> listXmppUser = commonDAO.search(search);
        return listXmppUser;
    }
}
