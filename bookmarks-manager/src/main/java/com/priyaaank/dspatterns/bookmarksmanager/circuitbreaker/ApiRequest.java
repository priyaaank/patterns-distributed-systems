package com.priyaaank.dspatterns.bookmarksmanager.circuitbreaker;

public class ApiRequest implements Comparable {

    Boolean isSuccess;
    Long epochInMillis;

    public ApiRequest(Boolean isSuccess, Long epochInMillis) {
        this.isSuccess = isSuccess;
        this.epochInMillis = epochInMillis;
    }

    public Boolean isSuccess() {
        return this.isSuccess;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof ApiRequest)) return -1;
        ApiRequest otherApiReq = (ApiRequest) o;
        if (this.equals(otherApiReq)) return 0;
        if (this.epochInMillis < otherApiReq.epochInMillis) return -1;
        if (this.epochInMillis == otherApiReq.epochInMillis) return 0;
        return 1;
    }

}