/*
	Milyn - Copyright (C) 2006 - 2010

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License (version 2.1) as published by the Free Software
	Foundation.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	See the GNU Lesser General Public License for more details:
	http://www.gnu.org/licenses/lgpl.txt
*/
package org.tfennelly.httpanalyse.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Collections Utilities.
 * 
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class CollectionsUtil {

    /**
     * Private constructor.
     */
    private CollectionsUtil() {
    }

    /**
     * Create an Object {@link java.util.Set} from the supplied objects.
     * @param objects The objects to be added to the set.
     * @return The {@link java.util.Set}.
     */
    public static <T> Set<T> toSet(T... objects) {
        Set<T> theSet = new LinkedHashSet<T>();
        addToCollection(theSet, objects);
        return theSet;
    }

    /**
     * Create an Object {@link java.util.List} from the supplied objects.
     * @param objects The objects to be added to the list.
     * @return The {@link java.util.List}.
     */
    public static <T> List<T> toList(T... objects) {
        List<T> theList = new ArrayList<T>();
        addToCollection(theList, objects);
        return theList;
    }

    /**
     * Create an Object {@link java.util.List} from the supplied Enumeration of objects.
     * @param objects The objects to be added to the list.
     * @return The {@link java.util.List}.
     */
    public static <T> List<T> toList(Enumeration<T> objects) {
        List<T> theList = new ArrayList<T>();
        addToCollection(theList, objects);
        return theList;
    }

    /**
     * Create an Object {@link java.util.List} from the supplied Enumeration of objects.
     * @param objects The objects to be added to the list.
     * @return The {@link java.util.List}.
     */
    public static <T> Set<T> toSet(Enumeration<T> objects) {
        Set<T> theSet = new LinkedHashSet<T>();
        addToCollection(theSet, objects);
        return theSet;
    }

    public static <T> void addToCollection(Collection<T> theCollection, T... objects) {
        if (objects != null) {
            for(T object : objects) {
                theCollection.add(object);
            }
        }
    }

    public static <T> void addToCollection(Collection<T> theCollection, Enumeration<T> objects) {
        if (objects != null) {
            while(objects.hasMoreElements()) {
                theCollection.add(objects.nextElement());
            }
        }
    }
}
