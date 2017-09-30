/*
* Created at 22:36 on 22/11/15
*/
package com.example.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author zzhao
 */
public final class CollectionUtil {

  private CollectionUtil() {
    throw new AssertionError("not for instantiation or inheritance");
  }

  public static <T> T last(List<T> list) {
    if (list.isEmpty()) {
      throw new NoSuchElementException();
    }
    return list.get(list.size() - 1);
  }

  /**
   * @param its a list of iterators, e.g. [ia, ib, ic], [ja, jb]
   * @return an interleaving iterator, e.g. with the elements [ia, ja, ib, jb, ic]
   */
  public static <T> Iterator<T> interleavingIterator(List<Iterator<T>> its) {
    return new Iterator<T>() {

      private List<Iterator<T>> itList = new ArrayList<>(its);

      private Iterator<Iterator<T>> itIt = this.itList.iterator();

      private Iterator<T> it;

      @Override
      public boolean hasNext() {
        while (this.itList.size() > 0) {
          while (this.itIt.hasNext()) {
            this.it = this.itIt.next();
            if (!this.it.hasNext()) {
              this.itIt.remove();
            } else {
              return true;
            }
          }
          this.itIt = this.itList.iterator();
        }
        return false;
      }

      @Override
      public T next() {
        if (!this.it.hasNext()) {
          throw new NoSuchElementException();
        }
        return this.it.next();
      }
    };
  }
}
