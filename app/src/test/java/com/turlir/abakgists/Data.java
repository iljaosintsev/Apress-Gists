package com.turlir.abakgists;


import com.turlir.abakgists.model.Gist;
import com.turlir.abakgists.model.GistModel;
import com.turlir.abakgists.model.GistOwner;

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

    Gist SERVER_STUB = new Gist(
            "85547e4878dd9a573215cd905650f284",
            "https://api.github.com/gists/85547e4878dd9a573215cd905650f284",
            "2017-04-27T21:54:24Z",
            "Part of setTextByParts",
            new GistOwner("iljaosintsev", "https://avatars1.githubusercontent.com/u/3526847?v=3")
    );

}
