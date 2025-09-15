package com.pokemon.rankings.dto;

import java.util.List;

public class GroupDetailResponse extends GroupResponse {
    private List<MemberRanking> leaderboard;

    public GroupDetailResponse() {
        super();
    }

    public GroupDetailResponse(GroupResponse groupResponse, List<MemberRanking> leaderboard) {
        super();
        this.setGroupId(groupResponse.getGroupId());
        this.setName(groupResponse.getName());
        this.setDescription(groupResponse.getDescription());
        this.setOwnerId(groupResponse.getOwnerId());
        this.setMembers(groupResponse.getMembers());
        this.setCreatedAt(groupResponse.getCreatedAt());
        this.leaderboard = leaderboard;
    }

    public List<MemberRanking> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(List<MemberRanking> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public static class MemberRanking {
        private String username;
        private double totalValue;
        private int cardCount;
        private int rank;

        public MemberRanking() {}

        public MemberRanking(String username, double totalValue, int cardCount, int rank) {
            this.username = username;
            this.totalValue = totalValue;
            this.cardCount = cardCount;
            this.rank = rank;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public double getTotalValue() {
            return totalValue;
        }

        public void setTotalValue(double totalValue) {
            this.totalValue = totalValue;
        }

        public int getCardCount() {
            return cardCount;
        }

        public void setCardCount(int cardCount) {
            this.cardCount = cardCount;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }
} 