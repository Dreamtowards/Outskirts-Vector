# Outskirts::Vector

vector math library in Outskirts System

## Performance

In this lib, ***ONLY 3 SITUATIONS*** will alloc heap memory(dynamic memory alloc by creating reference objects).

1. `public static final Type = new Type(xxx);` static constant.
2. `if (dest == null) dest = new Type();` in some static method's first two lines's check-new-dest.
3. `throw new ArithmeticException("...");` throwing exception(ArithmeticException only)
