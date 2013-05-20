/**
 *  Copyright 2009, 2010 The Regents of the University of California
 *  Licensed under the Educational Community License, Version 2.0
 *  (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at
 *
 *  http://www.osedu.org/licenses/ECL-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS"
 *  BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */

package org.opencastproject.util.data;

import org.apache.commons.lang.StringUtils;

import static org.opencastproject.util.data.Option.wrap;

/**
 * Hosts a collection of general purpose functions or function creating functions.
 */
public final class Functions {
  private Functions() {
  }

  /**
   * Noop function of arity 0.
   */
  public static <A> Function0<A> noop() {
    return new Function0<A>() {
      @Override
      public A apply() {
        return null;
      }
    };
  }

  /**
   * Identity function.
   */
  public static <B, A extends B> Function<A, B> identity() {
    return new Function<A, B>() {
      @Override
      public B apply(A a) {
        return a;
      }
    };
  }

  /**
   * Identity function returning always <code>a</code>.
   */
  public static <B, A extends B> Function0<B> identity(final A a) {
    return new Function0<B>() {
      @Override
      public B apply() {
        return a;
      }
    };
  }

  public static final Function<String, Option<String>> trimToNone = new Function<String, Option<String>>() {
    @Override
    public Option<String> apply(String s) {
      return wrap(StringUtils.trimToNull(s));
    }
  };
}
