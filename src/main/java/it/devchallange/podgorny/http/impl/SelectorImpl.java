package it.devchallange.podgorny.http.impl;

import it.devchallange.podgorny.http.Selector;

public class SelectorImpl implements Selector {
    private String domName, cssSelector;

    public SelectorImpl(String domName, String cssSelector) {
        this.domName = domName;
        this.cssSelector = cssSelector;
    }

    @Override
    public String getDOMName() {
        return domName;
    }

    public String getCSSSelector() {
        return cssSelector;
    }

}
