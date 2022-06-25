package com.jmsgvn.erbium.database.mongo;

import org.bson.Document;

public interface MongoResult {
    void call(Document document);
}
