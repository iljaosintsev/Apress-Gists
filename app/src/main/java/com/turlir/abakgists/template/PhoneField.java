package com.turlir.abakgists.template;

import android.content.Context;
import android.view.inputmethod.EditorInfo;

import ru.tinkoff.decoro.MaskDescriptor;
import ru.tinkoff.decoro.parser.PhoneNumberUnderscoreSlotsParser;
import ru.tinkoff.decoro.watchers.DescriptorFormatWatcher;
import ru.tinkoff.decoro.watchers.FormatWatcher;

public class PhoneField extends MaterialField {

    public PhoneField(Context context) {
        super(context);

        MaskDescriptor descriptor = MaskDescriptor.ofRawMask("+_ (___) ___-__-__")
                .setTerminated(true)
                .setHideHardcodedHead(true)
                .setForbidInputWhenFilled(true);

        FormatWatcher watcher = new DescriptorFormatWatcher(new PhoneNumberUnderscoreSlotsParser(), descriptor);
        if (getEditText() != null) {
            watcher.installOn(getEditText());
            getEditText().setInputType(EditorInfo.TYPE_CLASS_PHONE);
        }
    }
}
