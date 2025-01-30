package org.silo.community_management.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.silo.community_management.data.model.Budget;
import org.silo.community_management.data.model.BudgetFunds;
import org.silo.community_management.data.model.Event;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Setter
@Getter
public class CreateCommunityResponse {

    private String id;

    private String communityName;

    private String communityDescription;

    private byte[] imageVideoUrl;

    private LocalDateTime dateTimeOnboarded;

    private String dateFounded;

    private ArrayList<String> adminId = new ArrayList<>();

    private ArrayList<String> memberId =  new ArrayList<>();

    private ArrayList<Event> events = new ArrayList<>();

    private String founderName;

    private ArrayList<Budget> budgets = new ArrayList<>();

    private ArrayList<BudgetFunds> budgetFunds = new ArrayList<>();

    private String message;

}
