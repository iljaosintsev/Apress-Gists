package com.turlir.abakgists.template;


import android.support.annotation.Nullable;

class EditableProfile {

    final String name, position, contacts, additionalContact;

    @Nullable
    final String regEmail, regPhone;

    @Nullable
    final String url;

    final boolean showEmail;

    EditableProfile(String name, @Nullable String url, String position, String contacts, String additionalContact,
                            @Nullable String regEmail, boolean showEmail, @Nullable String regPhone) {
        this.name = name;
        this.url = url;
        this.position = position;
        this.contacts = contacts;
        this.additionalContact = additionalContact;
        this.regEmail = regEmail;
        this.regPhone = regPhone;
        this.showEmail = showEmail;
    }

    EditableProfile clone(String name, String position, String phone, String additionalContact) {
        return new EditableProfile(
                name,
                url,
                position,
                phone,
                additionalContact,
                regEmail,
                showEmail,
                regPhone
        );
    }

    public String letter() {
        return name.substring(0, 1);
    }

    public String phone() {
        return contacts
                .replaceAll("\\D+", "")
                .replaceFirst("^8", "+7");
    }

    public boolean urlCorrect() {
        return url != null && url.trim().length() > 0;
    }

    @Override
    public String toString() {
        return "EditableProfile { \n" +
               "    name = " + name + ",\n" +
               "    position = " + position + ",\n" +
               "    contacts = " + contacts + ",\n" +
               "    additionalContact = " + additionalContact + '\n' +
               '}';
    }
}
