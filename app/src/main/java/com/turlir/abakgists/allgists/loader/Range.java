package com.turlir.abakgists.allgists.loader;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;

import java.util.Objects;

public class Range implements Window, Parcelable {

    private static final int MAX_EL = 105;

    private final int absStart, absStop, addition;

    @VisibleForTesting
    public Range(int start, int stop) {
        this(start, stop, 15);
    }

    public Range(int start, int stop, int addition) {
        if (start >= stop) throw new IllegalArgumentException();
        absStart = start;
        absStop = stop;
        this.addition = addition;
    }

    //<editor-fold desc="Parcelable">

    private Range(Parcel in) {
        absStart = in.readInt();
        absStop = in.readInt();
        addition = in.readInt();
    }

    public static final Creator<Range> CREATOR = new Creator<Range>() {
        @Override
        public Range createFromParcel(Parcel in) {
            return new Range(in);
        }

        @Override
        public Range[] newArray(int size) {
            return new Range[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(absStart);
        dest.writeInt(absStop);
        dest.writeInt(addition);
    }

    //</editor-fold>

    @Override
    public int start() {
        return absStart;
    }

    @Override
    public int addition() {
        return addition;
    }

    @Override
    public int stop() {
        return absStop;
    }

    @Override
    public int count() {
        return absStop - absStart;
    }

    @Override
    public Window next() {
        return new Range(absStart + addition, absStop + addition, addition);
    }

    @Override
    public Window prev() {
        int start = Math.max(0, absStart - addition);
        return new Range(start, absStop - addition, addition);
    }

    @Override
    public boolean hasNext() {
        return absStop + addition <= MAX_EL;
    }

    @Override
    public boolean hasPrevious() {
        return absStart >= addition;
    }

    @Override
    public Range downScale(int coefficient) {
        if (coefficient < 2 || count() % coefficient != 0) throw new IllegalArgumentException();
        int newStart = absStart + count() / coefficient;
        return new Range(newStart, absStop, addition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return absStart == range.absStart &&
               absStop == range.absStop &&
               addition == range.addition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(absStart, absStop, addition);
    }

    @Override
    public String toString() {
        return "Range{" +
               "absStart=" + absStart +
               ", absStop=" + absStop +
               ", addition=" + addition +
               '}';
    }
}
