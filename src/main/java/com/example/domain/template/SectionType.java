package com.example.domain.template;

/**
 * @author zzhao
 */
public enum SectionType {
    Meta(1, "Metadata"),
    Description(2, "What is it about?"),
    Misc(3, "Other relevant information"),;

    private final int order;
    private final String description;

    SectionType(int order, String description) {
        this.order = order;
        this.description = description;
    }
}
