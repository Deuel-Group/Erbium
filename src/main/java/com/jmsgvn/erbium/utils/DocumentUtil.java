package com.jmsgvn.erbium.utils;

import com.jmsgvn.erbium.Erbium;
import lombok.Data;
import org.bson.Document;

public @Data class DocumentUtil {
    private Document document;
    private Erbium plugin;
    private String collection;
    private String field;
    private Object id;

    public DocumentUtil(Erbium plugin, String collection, String field, Object id) {
        this.plugin = plugin;
        this.collection = collection;
        this.field = field;
        this.id = id;

        get();
    }

    public void get() {
        plugin.getMongo().getDocument(false, collection, field, id, document -> {
            this.document = document;
        });
    }
}
