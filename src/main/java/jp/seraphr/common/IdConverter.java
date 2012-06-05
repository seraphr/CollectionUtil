/**
 *
 */
package jp.seraphr.common;

/**
 * 変換を行わないConverter実装
 */
public class IdConverter<_T> implements Converter<_T, _T> {
    @Override
    public _T convert(_T aSource) {
        return aSource;
    }
}
