package com.turlir.abakgists;


import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistOwnerJson;
import com.turlir.abakgists.model.GistModel;

public interface Data {

    GistLocal LOCAL_STUB = new GistLocal(
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

    GistModel USER_STUB_F = new GistModel(
            "85547e4878dd9a573215cd905650f284",
            "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
            "28 апреля 2017, 02:54",
            "Part of setTextByParts",
            "iljaosintsev",
            "https://avatars1.githubusercontent.com/u/3526847?v=3",
            "note",
            false
    );

    GistModel USER_STUB_T = new GistModel(
            "85547e4878dd9a573215cd905650f284",
            "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
            "28 апреля 2017, 02:54",
            "Part of setTextByParts",
            "iljaosintsev",
            "https://avatars1.githubusercontent.com/u/3526847?v=3",
            "note",
            true
    );

}
