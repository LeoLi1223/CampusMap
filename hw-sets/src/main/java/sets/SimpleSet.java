package sets;

import java.util.List;

/**
 * Represents an immutable set of points on the real line that is easy to
 * describe, either because it is a finite set, e.g., {p1, p2, ..., pN}, or
 * because it excludes only a finite set, e.g., R \ {p1, p2, ..., pN}. As with
 * FiniteSet, each point is represented by a Java float with a non-infinite,
 * non-NaN value.
 */
public class SimpleSet {

  // RI: complement != null and points != null
  // AF(this) = this.points      if complement = false
  //          = R \ this.points  if complement = true
  private final boolean complement;
  private final FiniteSet points;

  /**
   * Creates a simple set containing only the given points.
   * @param vals Array containing the points to make into a SimpleSet
   * @spec.requires points != null and has no NaNs, no infinities, and no dups
   * @spec.effects this = {vals[0], vals[1], ..., vals[vals.length-1]}
   */
  public SimpleSet(float[] vals) {
    this.complement = false;
    this.points = FiniteSet.of(vals);
  }

  /**
   * Private constructor that directly fills in the fields above.
   * @param complement Whether this = points or this = R \ points
   * @param points List of points that are in the set or not in the set
   * @spec.requires points != null
   * @spec.effects this = R \ points if complement else points
   */
  private SimpleSet(boolean complement, FiniteSet points) {
    this.complement = complement;
    this.points = points;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SimpleSet))
      return false;

    SimpleSet other = (SimpleSet) o;
    return this.complement == other.complement && this.points.equals(other.points);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Returns the number of points in this set.
   * @return N      if this = {p1, p2, ..., pN} and
   *         infty  if this = R \ {p1, p2, ..., pN}
   */
  public float size() {
    // Case 1: this.complement = true. Then, this represents the complement
    //         of the given set. Thus, it should return positive infinity.
    // Case 2: this.complement = false. Then, this represents the set of
    //         finitely many numbers. Thus, it should return the number of
    //         points in the set, which is the size of the points.
    return this.complement ? Float.POSITIVE_INFINITY : (float) this.points.size();
  }

  /**
   * Returns a string describing the points included in this set.
   * @return the string "R" if this contains every point,
   *     a string of the form "R \ {p1, p2, .., pN}" if this contains all
   *        but {@literal N > 0} points, or
   *     a string of the form "{p1, p2, .., pN}" if this contains
   *        {@literal N >= 0} points,
   *     where p1, p2, ... pN are replaced by the individual numbers.
   */
  public String toString() {
    // TODO: implement this with a loop. document its invariant
    //       a StringBuilder may be useful for creating the string
    List<Float> finiteSet = this.points.getPoints();
    if (!this.complement && finiteSet.isEmpty()) {
      return "{}";
    }

    if (this.complement && finiteSet.isEmpty()) {
      return "R";
    }

    StringBuilder sb = new StringBuilder();
    if (this.complement) {
      sb.append("R \\ ");
    }
    sb.append("{");

    // precondition:
    // (1)  sb = "R \ {" if this.complement = true
    //         = "{"     otherwise
    // (2)  finiteSet.size() > 0

    int i = 0;
    // Inv: sb = "R \ {p[0] p[1] ... p[i-1]" if this.complement = true
    //         = "{p[0] p[1] ... p[i-1]"      otherwise
    //      shortcut: p[i] = points[i] + ", "
    while (i < finiteSet.size()) {
      sb.append(finiteSet.get(i)).append(", ");
      i++;
    }

    sb.replace(sb.length() - 2, sb.length(), "}");
    return sb.toString();
  }

  /**
   * Returns a set representing the points R \ this.
   * @return R \ this
   */
  public SimpleSet complement() {
    // Case 1: this represents the given finite set of points, which is this.points.
    //         The constructor creates a new SimpleSet of R \ this.points, which matches
    //         the return of the method.
    // Case 2: this represents the complement of the given set, which is R \ this.points.
    //         The constructor creates a new SimpleSet of this.points = R \ (R \ this.points),
    //         which also matches the return of the method.
    return new SimpleSet(!this.complement, this.points);
  }

  /**
   * Returns the union of this and other.
   * @param other Set to union with this one.
   * @spec.requires other != null
   * @return this union other
   */
  public SimpleSet union(SimpleSet other) {
    // Case 1: Both sets are finite. Then, all elements in this.points and other.points
    //         should be included in the resulting set. Thus, the method should return
    //         the union of this.points and other.points.
    if (!this.complement && !other.complement) {
      return new SimpleSet(false, this.points.union(other.points));
    }
    // Case 2: Both set are finite complement. Then, this.points and other.points are
    //         the elements excluded in this and other. Thus, the intersection of them
    //         contains the elements that should be excluded in the union of this and
    //         others. In this case, the method should return the complement of the
    //         intersection of this.points and other.points.
    if (this.complement && other.complement) {
      return new SimpleSet(true, this.points.intersection(other.points));
    }
    // Case 3: this is finite and other is finite complement. Then, other.points are
    //         elements excluded. other.points - this.points are the elements need to
    //         exclude in the union. Thus, the method returns the complement of
    //         (other.points - this.points).
    if (!this.complement && other.complement) {
      return new SimpleSet(true, other.points.difference(this.points));
    }
    // Case 4: this is finite complement and other is finite. This case is similar to
    //         Case 3 except swapping this and other. Thus, applying the same logic,
    //         the method returns the complement of (this.points - other.points).
    return new SimpleSet(true, this.points.difference(other.points));
  }

  /**
   * Returns the intersection of this and other.
   * @param other Set to intersect with this one.
   * @spec.requires other != null
   * @return this intersect other
   */
  public SimpleSet intersection(SimpleSet other) {
    // Case 1: Both sets are finite. The method should simply return
    //         the FiniteSet intersection of two set of points.
    if (!this.complement && !other.complement) {
      return new SimpleSet(false, this.points.intersection(other.points));
    }
    // Case 2: Both sets are finite complement. Then, all points of this and
    //         other should be excluded in the intersection. Therefore, the method
    //         returns the complement of the union of the excluded points.
    if (this.complement && other.complement) {
      return new SimpleSet(true, this.points.union(other.points));
    }
    // Case 3: Only this set is finite complement. Then all points in this should be
    //         excluded. The method should return the finite set of points in other.points
    //         not in this.points.
    if (this.complement) {
      return new SimpleSet(false, other.points.difference(this.points));
    }
    // Case 4: Only other is finite complement. Applying similar logic in Case 3, the
    //         method should return the finite set of points in this.points but other.points.
    return new SimpleSet(false, this.points.difference(other.points));
  }

  /**
   * Returns the difference of this and other.
   * @param other Set to difference from this one.
   * @spec.requires other != null
   * @return this minus other
   */
  public SimpleSet difference(SimpleSet other) {
    // Case 1: Both sets are finite. Then, simply return the finite set of points in
    //         this not in other.
    if (!this.complement && !other.complement) {
      return new SimpleSet(false, this.points.difference(other.points));
    }
    // Case 2: Both sets are finite complement. Since we want the set of points in this
    //         not in other, only points in others.points can be included. Thus, the method
    //         returns a finite set of other.points - this.points.
    if (this.complement && other.complement) {
      return new SimpleSet(false, other.points.difference(this.points));
    }
    // Case 3: this is finite and other is finite complement. The resulting set should
    //         consist of points in this finite set not in other finite set. Thus, the
    //         method returns a finite set of this.points intersecting with other.points.
    if (!this.complement) {
      return new SimpleSet(false, this.points.intersection(other.points));
    }
    // Case 4: this is finite complement and other is finite. Points in both this finite
    //         set and other finite set should be excluded since this = R \ this.points and
    //         we want this \ other. Thus, the method returns the complement of the union
    //         of this finite set and other finite set.
    return new SimpleSet(true, this.points.union(other.points));
  }

}
