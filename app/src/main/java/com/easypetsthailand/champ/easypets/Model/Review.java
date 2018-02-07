package com.easypetsthailand.champ.easypets.Model;

import java.io.Serializable;

public class Review implements Serializable{

    private int reviewId;
    private String reviewerUid;
    private String reviewText;
    private String reviewPicturePath;
    private String timeReviewed;

    public Review(int reviewId, String reviewerUid, String reviewText, String reviewPicturePath, String timeReviewed) {
        this.reviewId = reviewId;
        this.reviewerUid = reviewerUid;
        this.reviewText = reviewText;
        this.reviewPicturePath = reviewPicturePath;
        this.timeReviewed = timeReviewed;
    }

    public int getReviewId() {
        return reviewId;
    }

    public String getReviewerUid() {
        return reviewerUid;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getReviewPicturePath() {
        return reviewPicturePath;
    }

    public String getTimeReviewed() {
        return timeReviewed;
    }

}
