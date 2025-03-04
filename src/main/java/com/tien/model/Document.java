package com.tien.model;

import java.io.Serializable;

public class Document implements Serializable {
    private static final long serialVersionUID = 1L;
    private String docId;
    private String title;
    private String uploaderId;
    private String fileName;

    public Document(String docId, String title, String uploaderId, String fileName) {
        this.docId = docId;
        this.title = title;
        this.uploaderId = uploaderId;
        this.fileName = fileName;
    }

    public String getDocId() {
        return docId;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    @Override
    public String toString() {
        return String.format("| %-8s | %-20s | %-10s | %-20s |", docId, title, uploaderId, fileName);
    }
}
