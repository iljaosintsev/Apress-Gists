package com.turlir.abakgists.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;
import com.turlir.abakgists.allgists.TypesFactory;
import com.turlir.abakgists.allgists.ViewModel;

@StorIOSQLiteType(table = GistsTable.GISTS)
public class GistModel extends ViewModel implements Parcelable {

    @StorIOSQLiteColumn(name = GistsTable.ID, key = true)
    @NonNull
    public String id;

    @StorIOSQLiteColumn(name = GistsTable.URL)
    @NonNull
    public String url;

    @StorIOSQLiteColumn(name = GistsTable.CREATED)
    @NonNull
    public String created;

    @StorIOSQLiteColumn(name = GistsTable.DESC)
    @Nullable
    public String description;

    @StorIOSQLiteColumn(name = GistsTable.OWNER_LOGIN)
    @Nullable
    public String ownerLogin;

    @StorIOSQLiteColumn(name = GistsTable.OWNER_AVATAR)
    @Nullable
    public String ownerAvatarUrl;

    @StorIOSQLiteColumn(name = GistsTable.NOTE)
    @Nullable
    public String note;

    public static final Creator<GistModel> CREATOR = new Creator<GistModel>() {
        @Override
        public GistModel createFromParcel(Parcel in) {
            return new GistModel(in);
        }

        @Override
        public GistModel[] newArray(int size) {
            return new GistModel[size];
        }
    };

    public GistModel() {
        // for storio, don`t use
    }

    public GistModel(@NonNull String id, @NonNull String url, @NonNull String created,
                     @Nullable String description) {
        this(id, url, created, description, null, null);
    }

    public GistModel(@NonNull String id, @NonNull String url, @NonNull String created,
                     @Nullable String description, @Nullable String ownerLogin, @Nullable String ownerAvatarUrl) {
        this(id, url, created, description, null, ownerLogin, ownerAvatarUrl);
    }

    public GistModel(@NonNull String id, @NonNull String url, @NonNull String created,
                     @Nullable String description, @Nullable String note, @Nullable String ownerLogin,
                     @Nullable String ownerAvatarUrl) {
        this.id = id;
        this.url = url;
        this.created = created;
        this.description = description;
        this.note = note;
        overrideOwnerLogin(ownerLogin);
        this.ownerAvatarUrl = ownerAvatarUrl;
    }

    public GistModel(GistModel other, String desc, String note) {
        this(other.id, other.url, other.created, other.description, other.note,
                other.ownerLogin, other.ownerAvatarUrl);
        description = desc;
        this.note = note;
    }

    @VisibleForTesting
    public GistModel(GistModel other) {
        this(other.id, other.url, other.created, other.description);
        if (other.note != null) {
            note = other.note;
        }
        if (other.ownerLogin != null) {
            ownerLogin = other.ownerLogin;
        }
        if (other.ownerAvatarUrl != null) {
            ownerAvatarUrl = other.ownerAvatarUrl;
        }
    }

    private GistModel(Parcel in) {
        url = in.readString();
        id = in.readString();
        description = in.readString();
        created = in.readString();
        note = in.readString();
        ownerLogin = in.readString();
        ownerAvatarUrl = in.readString();
    }

    @Override
    public int type(TypesFactory factory) {
        return factory.type(this);
    }

    public boolean doesOwnerLogin(GistModel item) {
        return !"anonymous".equals(item.ownerLogin);
    }

    /**
     * различны ли элементы с точки зрения базы данных
     * @param other сравниваемый элемент
     * @return {@code true} - различны, иначе {@code false}
     */
    public boolean isDifferent(GistModel other) {
        return !other.id.equals(this.id);
    }

    public Uri insteadWebLink() {
        return Uri.parse(String.format("http://gist.github.com/%s/%s", ownerLogin, id));
    }

    private void overrideOwnerLogin(@Nullable String ownerLogin) {
        if (ownerLogin != null) {
            this.ownerLogin = ownerLogin;
        } else {
            this.ownerLogin = "anonymous";
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(created);
        dest.writeString(note);
        dest.writeString(ownerLogin);
        dest.writeString(ownerAvatarUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GistModel gistModel = (GistModel) o;

        if (!id.equals(gistModel.id)) return false;
        if (!url.equals(gistModel.url)) return false;
        if (!created.equals(gistModel.created)) return false;

        if (description != null) {
            if (!description.equals(gistModel.description)) return false;
        } else {
            if (gistModel.description != null) return false;
        }

        if (ownerLogin != null) {
            if (!ownerLogin.equals(gistModel.ownerLogin)) return false;
        } else {
            if (gistModel.ownerLogin != null) return false;
        }

        if (ownerAvatarUrl != null) {
            if (!ownerAvatarUrl.equals(gistModel.ownerAvatarUrl)) return false;
        } else {
            if (gistModel.ownerAvatarUrl != null) return false;
        }

        if (note != null) {
            return note.equals(gistModel.note);
        }
        else {
            return gistModel.note == null;
        }
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 113 * result + url.hashCode();
        result = 113 * result + created.hashCode();
        result = 113 * result + (description != null ? description.hashCode() : 0);
        result = 113 * result + (ownerLogin != null ? ownerLogin.hashCode() : 0);
        result = 113 * result + (ownerAvatarUrl != null ? ownerAvatarUrl.hashCode() : 0);
        result = 113 * result + (note != null ? note.hashCode() : 0);
        return result;
    }
}
