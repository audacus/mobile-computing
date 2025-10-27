package ch.hftm.mobilecomputing.entity;

import java.util.Date;

public class Event {

    private String id;
    private Long picture;
    private Date startDate;
    private Date endDate;
    private Integer minPeople;
    private Integer maxPeople;
    private String description;
    private Integer rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPicture() {
        return picture;
    }

    public void setPicture(Long picture) {
        this.picture = picture;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getMinPeople() {
        return minPeople;
    }

    public void setMinPeople(Integer minPeople) {
        this.minPeople = minPeople;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
