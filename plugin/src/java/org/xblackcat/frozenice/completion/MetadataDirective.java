package org.xblackcat.frozenice.completion;

/**
 * 09.02.12 12:47
 *
 * @author xBlackCat
 */
class MetadataDirective {
    public static final MetadataDirective[] DIRECTIVES = new MetadataDirective[]{
            new MetadataDirective("amd"),
            new MetadataDirective("deprecated"),
            new MetadataDirective("protected"),
            new MetadataDirective("UserException"),
            new MetadataDirective("cpp:class"),
            new MetadataDirective("cpp:const"),
            new MetadataDirective("cpp:type:wstring"),
            new MetadataDirective("cpp:header-ext"),
            new MetadataDirective("cpp:include"),
            new MetadataDirective("cpp:virtual"),
            new MetadataDirective("java:package", true),
            new MetadataDirective("java:getset"),
            new MetadataDirective("java:serializable"),
            new MetadataDirective("java:type", true),
            new MetadataDirective("cs:attribute"),
            new MetadataDirective("clr:class"),
            new MetadataDirective("clr:collection"),
            new MetadataDirective("clr:generic:SortedDictionary"),
            new MetadataDirective("clr:generic"),
            new MetadataDirective("clr:property"),
            new MetadataDirective("clr:serializable"),
            new MetadataDirective("objc:prefix"),
            new MetadataDirective("python:package")
    };
    private final String directive;
    private final boolean hasArgument;

    private MetadataDirective(String directive) {
        this(directive, false);
    }

    private MetadataDirective(String directive, boolean hasArgument) {
        this.directive = directive;
        this.hasArgument = hasArgument;
    }

    public boolean isHasArgument() {
        return hasArgument;
    }

    public String getDirective() {
        return directive;
    }
}
