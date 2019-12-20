# Outskirts::Vector

vector math library in Outskirts System.



## Design



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



## Future Plan

the lib will keep never had @Deprecated stuff AND as pleasure as possible.
if necessary, we will make relevant modifications/changes.
