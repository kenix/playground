package com.example.domain.template;

/**
 * @author zzhao
 */
public enum SectionType {
    Meta(1, "Metadata"),
    Description(2, "What is it about?"),
    Risk(3, "Risk and return"),
    Misc(4, "Other relevant information"),;

    private final int order;

    private final String description;

    SectionType(int order, String description) {
        this.order = order;
        this.description = description;
    }

    public int getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }
}
