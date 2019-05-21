package me.coneseo.demorestclient.dto;

import java.util.List;
import java.util.Map;

public class SearchData {
    private List<BookDTO> documents;
    private Map<String, String> meta;

    public List<BookDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<BookDTO> documents) {
        this.documents = documents;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
}
