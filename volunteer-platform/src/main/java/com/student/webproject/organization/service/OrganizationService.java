package com.student.webproject.organization.service;

import com.student.webproject.common.response.Result;
import com.student.webproject.organization.dto.*;
import java.security.Principal;
import java.util.List;

public interface OrganizationService {
    Result<List<OrganizationSummaryDTO>> listOrganizations();
    Result<OrganizationDetailDTO> getOrganizationDetails(Long id, Principal principal);
    Result<Void> applyToOrganization(Long orgId, Principal principal);
    Result<Void> leaveOrganization(Long orgId, Principal principal);
    Result<List<PendingApprovalDTO>> listPendingApprovals(Long orgId, Principal principal);
    Result<Void> approveApplication(Long membershipId, Principal principal);
    Result<Void> rejectOrRemoveMember(Long membershipId, Principal principal);
}