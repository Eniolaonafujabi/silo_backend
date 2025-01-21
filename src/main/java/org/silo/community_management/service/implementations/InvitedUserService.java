package org.silo.community_management.service.implementations;

import org.silo.community_management.data.model.InvitedUser;
import org.silo.community_management.data.repo.InvitedUserRepo;
import org.springframework.stereotype.Service;

@Service
public class InvitedUserService {

    private final InvitedUserRepo repo;

    public InvitedUserService(InvitedUserRepo repo) {
        this.repo = repo;
    }

    public void saveInvitedUser(String email, String communityId, String role) {
        InvitedUser invitedUser = new InvitedUser();
        invitedUser.setEmail(email);
        invitedUser.setCommunityId(communityId);
        invitedUser.setRole(role);
        repo.save(invitedUser);
    }

    public InvitedUser getInvitedUser(String email) {
        return repo.findInvitedUserByEmail(email);
    }

    public void deleteInvitedUser(String email) {
        repo.deleteByEmail(email);
    }

    public boolean invitedUserExists(String email) {
        return repo.findInvitedUserByEmail(email) != null;
    }
}
