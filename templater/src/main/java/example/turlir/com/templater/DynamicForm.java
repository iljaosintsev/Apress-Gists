package example.turlir.com.templater;

import android.content.Context;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import example.turlir.com.templater.base.Form;
import example.turlir.com.templater.base.Grouper;
import example.turlir.com.templater.widget.WidgetFactory;

public abstract class DynamicForm<T> implements Form<T> {

    private final ViewGroup mGroup;

    private final Structure<T> mStructure;
    private final WidgetFactory mFactory;

    private T mValue;

    private Template<T> mTemplate;

    public DynamicForm(@NonNull ViewGroup group) {
        mGroup = group;
        mStructure = createTemplate();
        mFactory = new WidgetFactory(group.getContext());
    }

    @Override
    public final void create() {
        mGroup.removeAllViews();
        List<WidgetHolder> holders = new ArrayList<>(mStructure.count());
        while (mStructure.hasNext()) {
            WidgetHolder h = mStructure.next(mFactory);
            holders.add(h);
        }
        mTemplate = new Template<>(holders, mStructure.outs(), mStructure.groups());
    }

    @Override
    public final void bind(@NonNull T value) {
        if (mTemplate == null) {
            throw new IllegalStateException("bind() before widget()");
        }
        if (mGroup.getChildCount() < 1) {
            throw new IllegalStateException("bind() before connect()");
        }
        mValue = value;
        mTemplate.bind();
        interact();
    }

    @Override
    public final void showError(String tag, String message) {
        WidgetHolder h = mTemplate.findHolder(tag);
        if (h != null) {
            h.showError(message);
        }
    }

    @Override
    public final void enabled(String tag, boolean state) {
        WidgetHolder h = mTemplate.findHolder(tag);
        if (h != null) {
            h.enabled(state);
        }
    }

    @Override
    public final void enabledAll(boolean state) {
        mTemplate.enabledAll(state);
    }

    @Override
    public final void visibility(String tag, int visibility) {
        WidgetHolder h = mTemplate.findHolder(tag);
        if (h != null) {
            h.visibility(visibility);
        }
    }

    @Override
    public /*final*/ void connect() {
        if (mTemplate == null) {
            throw new IllegalStateException("connect() before widget()");
        }
        mTemplate.connect(mGroup, group());
    }

    @Override
    public boolean verify() {
        if (mTemplate == null) {
            throw new IllegalStateException("verify() before widget()");
        }
        return mTemplate.verify();
    }

    @NonNull
    @Override
    public T collect() {
        mTemplate.collect(mValue);
        return mValue;
    }

    protected abstract Grouper group();

    protected abstract Structure<T> createTemplate();

    protected abstract void interact();

    protected final T value() {
        if (mValue == null) {
            throw new IllegalStateException();
        }
        return mValue;
    }

    protected final Context context() {
        return mGroup.getContext();
    }

    protected TextView.OnEditorActionListener doneVerifierListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    verify();
                }
                return false;
            }
        };
    }

}
