package com.example.musadkhan.etherscanaddressserch;

import java.math.BigDecimal;

public class model {

    String from,to,tokenName,tokenSymbol,hashNumber,status,timeStamp,scannerName;
    int blockNumber;
    BigDecimal tokenAmount;

    public model(String from, String to, String tokenName, String tokenSymbol, String hashNumber, String status, String timeStamp, String scannerName, int blockNumber, BigDecimal tokenAmount) {
        this.from = from;
        this.to = to;
        this.tokenName = tokenName;
        this.tokenSymbol = tokenSymbol;
        this.hashNumber = hashNumber;
        this.status = status;
        this.timeStamp = timeStamp;
        this.scannerName = scannerName;
        this.blockNumber = blockNumber;
        this.tokenAmount = tokenAmount;
    }

    public String getScannerName() {
        return scannerName;
    }

    public void setScannerName(String scannerName) {
        this.scannerName = scannerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getHashNumber() {
        return hashNumber;
    }

    public void setHashNumber(String hashNumber) {
        this.hashNumber = hashNumber;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public BigDecimal getTokenAmount() {
        return tokenAmount;
    }

    public void setTokenAmount(BigDecimal tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
