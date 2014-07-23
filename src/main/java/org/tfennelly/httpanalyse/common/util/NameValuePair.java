package org.tfennelly.httpanalyse.common.util;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class NameValuePair<V> {

    public String name;
    public V value;

    public NameValuePair(String name, V value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public V getValue() {
        return value;
    }
}
