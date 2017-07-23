package com.turlir.abakgists;


import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistOwnerJson;
import com.turlir.abakgists.model.GistModel;

public interface Data {

    GistModel LOCAL_STUB = new GistModel(
            "85547e4878dd9a573215cd905650f284",
            "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
            "2017-04-27T21:54:24Z",
            "Part of setTextByParts",
            "note",
            "iljaosintsev",
            "https://avatars1.githubusercontent.com/u/3526847?v=3"
    );

    GistJson SERVER_STUB = new GistJson(
            "85547e4878dd9a573215cd905650f284",
            "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
            "2017-04-27T21:54:24Z",
            "Part of setTextByParts",
            new GistOwnerJson("iljaosintsev", "https://avatars1.githubusercontent.com/u/3526847?v=3")
    );

}
