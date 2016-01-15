/*
* vwd KL
* Created by zzhao on 1/15/16 12:11 PM
*/
package com.example.misc;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Clearly an enum is the best type for gender. This example shows the usage of writeReplace and
 * readResolve respectively.
 *
 * @author zzhao
 */
public class Gender implements Serializable {

    public static final Gender MALE = new Gender("Male");

    public static final Gender FEMALE = new Gender("Female");

    private final String name;

    public Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    Object writeReplace() throws ObjectStreamException {
        return this.equals(MALE) ? SerializedForm.MALE_FORM : SerializedForm.FEMALE_FORM;
    }

    private static class SerializedForm implements Serializable {

        private static final SerializedForm MALE_FORM = new SerializedForm(0);

        private static final SerializedForm FEMALE_FORM = new SerializedForm(1);

        private final int value;

        public SerializedForm(int value) {
            this.value = value;
        }

        Object readResolve() throws ObjectStreamException {
            return this.value == MALE_FORM.value ? Gender.MALE : Gender.FEMALE;
        }
    }
}
