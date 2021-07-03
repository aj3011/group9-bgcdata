package com.example;

import java.lang.String;
import java.lang.Integer;
import java.util.ArrayList;

public class WorkItem {
    private String ItemName;
    private String StartDate;
    private String EndDate;
    private ArrayList<Team> TeamsAssigned;
    private String ItemType;
    private String FundingInformation;

    public String getItemName() {
        return this.ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public String getStartDate() {
        return this.StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getEndDate() {
        return this.EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public ArrayList<Team> getTeamsAssigned() {
        return this.TeamsAssigned;
    }

    public void setTeamsAssigned(ArrayList<Team> TeamsAssigned) {
        this.TeamsAssigned = TeamsAssigned;
    }

    public String getItemType() {
        return this.ItemType;
    }

    public void setItemType(String ItemType) {
        this.ItemType = ItemType;
    }

    public String getFundingInformation() {
        return this.FundingInformation;
    }

    public void setFundingInformation(String FundingInformation) {
        this.FundingInformation = FundingInformation;
    }

    private class Team {
        private String teamname;
        private ArrayList<Integer> Weights;

        public String getteamname() {
            return this.teamname;
        }

        public void setteamname(String teamname) {
            this.teamname = teamname;
        }

        public ArrayList<Integer> getWeights() {
            return this.Weights;
        }

        public void setWeights(ArrayList<Integer> Weights) {
            this.Weights = Weights;
        }
    }

}
