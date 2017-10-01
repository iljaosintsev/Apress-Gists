package com.turlir.abakgists.templater;

import android.content.Context;

public class Form extends BaseForm {

    private final CompleteCallback mOutside;

    private LabeledEditText mFirst;
    private VerticalEditText mSecond;

    public Form(Context cnt, CompleteCallback outside) {
        super(cnt);
        mOutside = outside;
    }

    public void showSecondFieldError(String msg) {
        mSecond.showError(msg);
    }

    @Override
    protected Template template() {
        return new Template.Builder(getContext())
                .addField("Label", "Content", new Callback<LabeledEditText>() {
                    @Override
                    public void added(LabeledEditText view) {
                        mFirst = view;
                    }
                })
                .addVerticalField("Label 2", "Static content", new Callback<VerticalEditText>() {
                    @Override
                    public void added(VerticalEditText view) {
                        mSecond = view;
                    }
                })
                .build();
    }

    @Override
    protected void interact() {
        mFirst.et.setText("Dynamic content");
    }

    @Override
    protected void verified() {
        ComplexValue cv = new ComplexValue(
                mFirst.content(),
                mSecond.content()
        );
        mOutside.onComplete(cv);
    }

    public interface CompleteCallback {
        void onComplete(ComplexValue value);
    }

    public static class ComplexValue {

        public final String first, second;

        public ComplexValue(String first, String second) {
            this.first = first;
            this.second = second;
        }
    }
}
