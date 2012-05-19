package jp.seraphr.collection;

public interface CollectionWrapper<_Base, _Element> {
    public <_ToElem, _To> CollectionWrapper<_To, _ToElem> map(Converter<_Element, _ToElem> aConverter);

    public _Base unwrap();
}
