# Outskirts::Vector

vector math library in Outskirts System.



## Design

### rules
tooltype-overload-method position:
if mainly(impl) method are public, then the tooltype overload will closely followed by(below) the mianly method. 
if mainly method are public, then the tooltype overload will just above the mainly method.

## Performance

### Space

In this lib, ***ONLY 3 SITUATIONS***! will alloc heap memory.

1. `public static final Type = new Type(xxx);` static constant.
2. `if (dest == null) dest = new Type();` in some static method's first two lines's check-new-dest.
3. `throw new ArithmeticException("...");` throwing exception(ArithmeticException only).

No.1 situation are just perform once or zero times from beginning to end.

No.2 situation are dependent you, actually in concept, this 'new' alloc are operation from you, you choose 'null' param just equals choose 'new Type()' param, the lib also can not supports null-dest and requires you take non-null dest param to those methods for not alloc heap in this situation, but the lib supports "null-dest-param as new Dest_Type() param" mainlly is for code looks Clear! that could makes your code more looks better!

No.3 situation are can't be happen in commonly. that just some like divide by zero.

### Time

we are super care the Clear Design. 

bt sometimes, duplicated-code AND dispersion-code inevitable exists for more higher performance.

In this lib, have 3 class(5 places) had duplicated code: Quaternion, Matrix4f(dup from Matrix3f)

1. Matrix4f.rotate(float, Vector3f, Matrix4f) dupfrom: Matrix3f.rotate(float, Vector3f, Matrix3f)
2. Quaternion.toMatrix(Quaternion, Matrix4f) dupfrom: Quaternion.toMatrix(Quaternion, Matrix3f)
3. Quaternion.rotate(float, Vector3f, Quaternion) dupfrom: Quaternion.fromAxisAngle() AND Quaternion.mul()
4. Quaternion.rotate(Quaternion, Matrix3f) dupfrom: Quaternion.toMatrix() AND Matrix3f.mul()
5. Quaternion.rotate(Quaternion, Matrix4f) dupfrom: Quaternion.toMatrix() AND {Matrix4f.mul3x3()}(wrapped)

bt those place are totually tooltype method, not mainly content. so hardly or just can't affect mainlly content.
and those duplicated code's method are been isolation to the bottom, and stay away from mainlly content. and every those duplicated code  unit had a note that pointing the duplication is from where src place. and those impl place is totually warpped, its invisible from outer. so that is very fine!

## Future

the lib will keep never had @Deprecated stuff AND as pleasure as possible.
if necessary, we will make relevant modifications/changes.
