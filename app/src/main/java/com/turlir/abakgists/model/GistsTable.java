package com.turlir.abakgists.model;

import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

public interface GistsTable {

    Query REQUEST_ALL = Query
            .builder()
            .table(GistsTable.GISTS)
            .build();

    DeleteQuery DELETE_ALL = DeleteQuery
            .builder()
            .table(GistsTable.GISTS)
            .build();

    String
            BASE_NAME = "gists_db",
            GISTS = "gists",
            ID = "id",
            DESC = "desc",
            URL = "url",
            CREATED = "created",
            OWNER_LOGIN = "ownerLogin",
            OWNER_AVATAR = "ownerAvatarUrl",
            NOTE = "note";

    String CREATE =
            "CREATE TABLE IF NOT EXISTS " + GISTS + "( " +
                    ID + " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE, " +
                    DESC + " TEXT, " +
                    URL + " TEXT NOT NULL, " +
                    CREATED + " TEXT NOT NULL, " +
                    OWNER_LOGIN + " TEXT, " +
                    OWNER_AVATAR + " TEXT, " +
                    NOTE + " TEXT " +
                    ")";

}