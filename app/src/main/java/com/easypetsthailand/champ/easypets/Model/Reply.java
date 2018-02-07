package com.easypetsthailand.champ.easypets.Model;

public class Reply {

    private int replyId;
    private String replyText;
    private String replyPicturePath;
    private String timeReplied;

    public Reply(int replyId, String replyText, String replyPicturePath, String timeReplied) {
        this.replyId = replyId;
        this.replyText = replyText;
        this.replyPicturePath = replyPicturePath;
        this.timeReplied = timeReplied;
    }

    public int getReplyId() {
        return replyId;
    }

    public String getReplyText() {
        return replyText;
    }

    public String getReplyPicturePath() {
        return replyPicturePath;
    }

    public String getTimeReplied() {
        return timeReplied;
    }

}
