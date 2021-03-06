/*
* vwd KL
* Created by zzhao on 1/15/16 11:20 AM
*/
package com.example.cli;

import com.example.util.thc.TypeRef;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zzhao
 */
public class TypeRefCli {


  public static void main(String[] args) {
    final Favorites favorites = new Favorites();
    favorites.setFavorite(String.class, "Java");
    favorites.setFavorite(Integer.class, 0xCAFEBABE);
    System.out.println(favorites.getFavorite(String.class));
    System.out.println(favorites.getFavorite(Integer.class));

    final TypeRef<ArrayList<String>> typeRef = new TypeRef<ArrayList<String>>() {
    };
    final List<String> stringList = typeRef.newInstance();
    stringList.add("foo");
    System.out.println(stringList);
  }

  public static class Favorites {

    private Map<Class<?>, Object> favorites = new HashMap<>();

    /**
     * Type erasure rears its ugly head: <tt>f.setFavorite(List<String>.class,
     * Collections.emptyList());</tt>
     */
    public <T> void setFavorite(Class<T> clazz, T thing) {
      this.favorites.put(clazz, thing);
    }

    public <T> T getFavorite(Class<T> clazz) {
      return clazz.cast(this.favorites.get(clazz));
    }
  }
}
