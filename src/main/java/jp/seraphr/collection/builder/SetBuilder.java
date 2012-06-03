/**
 *
 */
package jp.seraphr.collection.builder;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class SetBuilder<_Elem> extends CollectionBuilder<_Elem, Set<_Elem>> {
    private SetBuilder() {
        super(new HashSet<_Elem>());
    }
}
