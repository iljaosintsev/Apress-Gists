package com.turlir.abakgists;


import com.turlir.abakgists.api.data.GistJson;
import com.turlir.abakgists.api.data.GistLocal;
import com.turlir.abakgists.api.data.GistOwnerJson;

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

    GistJson NEW_SERVER = new GistJson(
            "1a1dc91c907325c69271ddf0c944bc72",
            "https://api.github.com/gists/1a1dc91c907325c69271ddf0c944bc72",
            "2017-04-27T21:54:24Z",
            "new element",
            new GistOwnerJson("robot", "https://avatars1.githubusercontent.com/u/3526847?v=3")
    );

    // TODO: iterator + optional mapper, build method for getting single item

}
