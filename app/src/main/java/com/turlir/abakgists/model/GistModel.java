package com.turlir.abakgists.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.turlir.abakgists.allgists.view.TypesFactory;
import com.turlir.abakgists.allgists.view.ViewModel;
import com.turlir.abakgists.api.data.GistLocal;

public class GistModel
        extends ViewModel
        implements Parcelable {

    public String id;

    public String url;

    public String created;

    public String description;

    public String ownerLogin;

    public String ownerAvatarUrl;

    public String note;

    public boolean isLocal;

    public static final Parcelable.Creator<GistModel> CREATOR = new Parcelable.Creator<GistModel>() {
        @Override
        public GistModel createFromParcel(Parcel in) {
            return new GistModel(in);
        }

        @Override
        public GistModel[] newArray(int size) {
            return new GistModel[size];
        }
    };

    public GistModel(GistLocal local, boolean isLocal) {
        id = local.id;
        url = local.url;
        created = local.created;
        description = local.description;
        ownerLogin = local.ownerLogin;
        ownerAvatarUrl = local.ownerAvatarUrl;
        note = local.note;
        this.isLocal = isLocal;
    }

    public GistModel(GistModel other, @NonNull String desc, @NonNull String note) {
        id = other.id;
        url = other.url;
        created = other.created;
        ownerLogin = other.ownerLogin;
        ownerAvatarUrl = other.ownerAvatarUrl;
        isLocal = other.isLocal;

        this.description = desc;
        this.note = note;
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
    public int type(TypesFactory factory) {
        return factory.type(this);
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

    public boolean doesOwnerLogin(GistModel item) {
        return !"anonymous".equals(item.ownerLogin);
    }

    @Override
    public String toString() {
        return "GistModel{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                '}';
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

}
