package com.argusoft.hkg.sync;

import com.argusoft.hkg.sql.sync.model.SyncXmppUser;
import java.util.List;

/**
 *
 * @author shruti
 */
public interface SyncXmppUserService {

    public Boolean createUser(SyncXmppUser hkXmppUser);

    public void updateUser(SyncXmppUser user);

    public void deleteUser(Long id);

    public SyncXmppUser getUserbyUserName(String userName);

    public SyncXmppUser getUserbyFranchise(Long franchiseId);

    public String resetPassword(String userId, String newPassword);

    public void activateUser(String userName);

    public void deActiveUser(String userName);

    public List<SyncXmppUser> retrieveUserByUserCreatedInTigase(Boolean userCreatedInTigase);

    public List<SyncXmppUser> retrieveAllActiveUsers();

}
