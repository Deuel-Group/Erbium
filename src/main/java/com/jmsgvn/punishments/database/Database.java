package com.jmsgvn.punishments.database;

import com.jmsgvn.punishments.database.mongo.MongoDeserializedResult;
import com.jmsgvn.punishments.profiles.Profile;
import com.jmsgvn.punishments.punishments.Punishment;

import java.util.UUID;

public abstract class Database {
    public enum Type {
        MONGO, SQLite, MySQL
    }

    public abstract Type type();

    public abstract Profile loadProfile(boolean async, String name, boolean store, MongoDeserializedResult mdr);
    public abstract Profile loadProfile(boolean async, UUID uuid, boolean store, MongoDeserializedResult mdr);

    public abstract void saveProfile(boolean async, Profile profile);

    public abstract void loadPunishment(boolean async, UUID uuid, boolean store);
    public abstract void savePunishment(boolean async, Punishment punishment);

    public abstract void close();
}
