package jp.seraphr.collection
import jp.seraphr.common.Converter
import jp.seraphr.common.Equivalence
import jp.seraphr.common.Predicate
import jp.seraphr.common.Tuple2
import jp.seraphr.common.Converter2

object TestUtils {
  implicit def funcToConvertor[_F, _T](aFunc: _F => _T): Converter[_F, _T] = {
    return new Converter[_F, _T] {
      override def convert(aFrom: _F): _T = aFunc(aFrom)
    }
  }

  implicit def funcToConvertor2[_F, _F2, _T](aFunc: (_F, _F2) => _T): Converter2[_F, _F2, _T] = {
    return new Converter2[_F, _F2, _T] {
      override def convert(aArg1: _F, aArg2: _F2): _T = aFunc(aArg1, aArg2)
    }
  }

  implicit def funcToPredicate[_F](aFunc: _F => Boolean): Predicate[_F] = {
    return new Predicate[_F] {
      override def apply(aFrom: _F): Boolean = aFunc(aFrom)
    }
  }

  implicit def funcToEquivalence[_F1, _F2](aFunc: (_F1, _F2) => Boolean): Equivalence[_F1, _F2] = {
    return new Equivalence[_F1, _F2] {
      override def apply(aLeft: _F1, aRight: _F2): Boolean = aFunc(aLeft, aRight)
    }
  }

  implicit def tupleToTuple[_E1, _E2](aFrom: (_E1, _E2)): Tuple2[_E1, _E2] = aFrom match {
    case (tE1, tE2) => Tuple2.create(tE1, tE2)
  }
}