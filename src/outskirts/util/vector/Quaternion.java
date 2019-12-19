package outskirts.util.vector;

public class Quaternion extends Vector4f {

    public static final Quaternion IDENTITY = new Quaternion().setIdentity();

    public Quaternion() {
        setIdentity();
    }

    public Quaternion(Quaternion src) {
        set(src);
    }

    public Quaternion(float x, float y, float z, float w) {
        set(x, y, z, w);
    }

    @Override
    public Quaternion set(Vector4f src) { //should Quaternion only..?
        return set(src.x, src.y, src.z, src.w);
    }

    @Override
    public Quaternion set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    @Override
    public Quaternion scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        this.w *= scalar;
        return this;
    }

    @Override
    public Quaternion negate() { //conjugate
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        //not affect to w
        return this;
    }

    @Override
    public Quaternion normalize() {
        return (Quaternion) Vector.normalize(this);
    }

    @Override
    public String toString() {
        return "Quaternion[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Quaternion && ((Quaternion) obj).x == this.x && ((Quaternion) obj).y == this.y && ((Quaternion) obj).z == this.z && ((Quaternion) obj).w == this.w;
    }

    @Override
    public Quaternion setX(float x) {
        this.x = x;
        return this;
    }

    @Override
    public Quaternion setY(float y) {
        this.y = y;
        return this;
    }

    @Override
    public Quaternion setZ(float z) {
        this.z = z;
        return this;
    }

    @Override
    public Quaternion setW(float w) {
        this.w = w;
        return this;
    }

    @Override
    public Quaternion add(Vector4f right) {
        return add(right.x, right.y, right.z, right.w);
    }

    @Override
    public Quaternion add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }

    @Override
    public Quaternion sub(Vector4f right) {
        return sub(right.x, right.y, right.z, right.w);
    }

    @Override
    public Quaternion sub(float x, float y, float z, float w) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.w -= w;
        return this;
    }

    public Quaternion setIdentity() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
        this.w = 1f;
        return this;
    }

    public Quaternion inverse() {
        float invSq = 1.0F / lengthSquared();
        this.x *= -invSq;
        this.y *= -invSq;
        this.z *= -invSq;
        this.w *= invSq;
        return this;
    }

    public static Quaternion mul(Quaternion left, Quaternion right, Quaternion dest) {
        if (dest == null)
            dest = new Quaternion();
        return dest.set(
                left.x * right.w + left.w * right.x + left.y * right.z - left.z * right.y,
                left.y * right.w + left.w * right.y + left.z * right.x - left.x * right.z,
                left.z * right.w + left.w * right.z + left.x * right.y - left.y * right.x,
                left.w * right.w - left.x * right.x - left.y * right.y - left.z * right.z
        );
    }



    // EXTRA CONVERT FUNCTION

    public static Quaternion fromAxisAngle(Vector4f a, Quaternion dest) {
        return fromAxisAngle(new Vector3f(a.x, a.y, a.z), a.w, dest);
    }

    public static Quaternion fromAxisAngle(Vector3f axis, float angle, Quaternion dest) {
        if (dest == null)
            dest = new Quaternion();
        float s = (float)Math.sin(angle * 0.5f) / axis.length();
        return dest.set(
                axis.x * s,
                axis.y * s,
                axis.z * s,
                (float)Math.cos(angle * 0.5f)
        );
    }

    /**
     * @param q required normalized src quaternion.
     */
    public static Vector4f toAxisAngle(Quaternion q, Vector4f dest) {
        if (dest == null)
            dest = new Vector4f();
        float s = (float)Math.sqrt(1f - q.w * q.w);
        if (s == 0)
            throw new ArithmeticException("Identity quaternion.");
        return dest.set(
                q.x / s,
                q.y / s,
                q.z / s,
                (float)Math.acos(q.w) * 2f
        );
    }

    public static Quaternion fromMatrix(Matrix3f mat, Quaternion dest) {
        return fromMatrix(mat.m00, mat.m01, mat.m02, mat.m10, mat.m11, mat.m12, mat.m20, mat.m21, mat.m22, dest);
    }

    public static Quaternion fromMatrix(Matrix4f mat, Quaternion dest) {
        return fromMatrix(mat.m00, mat.m01, mat.m02, mat.m10, mat.m11, mat.m12, mat.m20, mat.m21, mat.m22, dest);
    }

    /**
     * just like Matrix3f
     * m00 m01 m02
     * m10 m11 m12
     * m20 m21 m22
     */
    private static Quaternion fromMatrix(float m00, float m01, float m02,
                                         float m10, float m11, float m12,
                                         float m20, float m21, float m22, Quaternion dest) {
        if (dest == null)
            dest = new Quaternion();

        float tr = m00 + m11 + m12;
        float s;
        if (tr >= 0f) {
            s = (float) Math.sqrt(tr + 1f);
            dest.w = s * 0.5f;
            s = 0.5f / s;
            dest.x = (m12 - m21) * s;
            dest.y = (m20 - m02) * s;
            dest.z = (m01 - m10) * s;
        } else {
            float max = Math.max(Math.max(m00, m11), m22);
            if (max == m00) {
                s = (float) Math.sqrt(m00 - (m11 + m22) + 1f);
                dest.x = s * 0.5f;
                s = 0.5f / s;
                dest.y = (m10 + m01) * s;
                dest.z = (m02 + m20) * s;
                dest.w = (m12 - m21) * s;
            } else if (max == m11) {
                s = (float) Math.sqrt(m11 - (m22 + m00) + 1f);
                dest.y = s * 0.5f;
                s = 0.5f / s;
                dest.z = (m21 + m12) * s;
                dest.x = (m10 + m01) * s;
                dest.w = (m20 - m02) * s;
            } else {
                s = (float) Math.sqrt(m22 - (m00 + m11) + 1f);
                dest.z = s * 0.5f;
                s = 0.5f / s;
                dest.x = (m02 + m20) * s;
                dest.y = (m21 + m12) * s;
                dest.w = (m01 - m10) * s;
            }
        }
        return dest;
    }

    public static Matrix3f toMatrix(Quaternion q, Matrix3f dest) {
        if (dest == null)
            dest = new Matrix3f();
        float d = q.lengthSquared();
        if (d == 0.0F)
            throw new ArithmeticException("Zero length quaternion.");
        float s = 2f / d;
        float xs = q.x * s, ys = q.y * s, zs = q.z * s;
        float wx = q.w * xs, wy = q.w * ys, wz = q.w * zs;
        float xx = q.x * xs, xy = q.x * ys, xz = q.x * zs;
        float yy = q.y * ys, yz = q.y * zs, zz = q.z * zs;
        dest.m00 = 1f - (yy + zz);
        dest.m01 = xy - wz;
        dest.m02 = xz + wy;
        dest.m10 = xy + wz;
        dest.m11 = 1f - (xx + zz);
        dest.m12 = yz - wx;
        dest.m20 = xz - wy;
        dest.m21 = yz + wx;
        dest.m22 = 1f - (xx + yy);
        return dest;
    }

    // duplicated code from Quaternion::toMatrix(Quaternion, Matrix3f). just for no heap alloc
    // == Matrix4f.set3x3(Quaternion.toMatrix(q, new Matrix3f()))
    /**
     * note that this will only directly set(not mul) Matrix4x4's first 0-2 rows AND cols (9 elements), other elements will not be touch.
     */
    @SuppressWarnings("all")
    public static Matrix4f toMatrix(Quaternion q, Matrix4f dest) {
        if (dest == null)
            dest = new Matrix4f();
        float d = q.lengthSquared();
        if (d == 0.0F)
            throw new ArithmeticException("Zero length quaternion.");
        float s = 2f / d;
        float xs = q.x * s, ys = q.y * s, zs = q.z * s;
        float wx = q.w * xs, wy = q.w * ys, wz = q.w * zs;
        float xx = q.x * xs, xy = q.x * ys, xz = q.x * zs;
        float yy = q.y * ys, yz = q.y * zs, zz = q.z * zs;
        dest.m00 = 1f - (yy + zz);
        dest.m01 = xy - wz;
        dest.m02 = xz + wy;
        dest.m10 = xy + wz;
        dest.m11 = 1f - (xx + zz);
        dest.m12 = yz - wx;
        dest.m20 = xz - wy;
        dest.m21 = yz + wx;
        dest.m22 = 1f - (xx + yy);
        return dest;
    }




    // EXTRA CRAZY DUPLICATION TOOL FUNCTION.
    // (but its fine, this is full duplicate from one src code, and this impl is private wrapped.
    //  but a lots convenient AND non heap alloc! so space effective. (except when null-dest or throw Exception.))
    
    
    // dup from Quaternion::fromAxisAngle AND Quaternion::mul
    // == Quaternion.mul(q, Quaternion.fromAxisAngle(angle, axis, new Quaternion()))
    /**
     * integrate dest quaternion
     * e.g use for take EulerAngles to integrate/convert to a Quaternion 
     */
    @SuppressWarnings("all")
    public static Quaternion rotate(float angle, Vector3f axis, Quaternion dest) {
        if (dest == null)
            dest = new Quaternion();
        // Quaternion.fromAxisAngle(axis.xyz, angle, new Quaternion())
        float s = (float) Math.sin(angle * 0.5f) / axis.length();
        float rx = axis.x * s;
        float ry = axis.y * s;
        float rz = axis.z * s;
        float rw = (float)Math.cos(angle * 0.5f);
        // Quaternion.mul(dest, aq)
        return dest.set(
                dest.x * rw + dest.w * rx + dest.y * rz - dest.z * ry,
                dest.y * rw + dest.w * ry + dest.z * rx - dest.x * rz,
                dest.z * rw + dest.w * rz + dest.x * ry - dest.y * rx,
                dest.w * rw - dest.x * rx - dest.y * ry - dest.z * rz
        );
    }

    // == Matrix3f.mul(dest, Quaternion.toMatrix(q, new Matrix3f()))
    /**
     * integrate dest Matrix3f
     * e.g use for take a Quaternion to integrate/convert a Matrix3f. like take rigidBody.angVel to Quaternion then integrate Matrix3f transform.basis
     */
    @SuppressWarnings("all")
    public static Matrix3f rotate(Quaternion q, Matrix3f dest) {
        // Quaternion.toMatrix(q, new Matrix3f())
        if (dest == null)
            dest = new Matrix3f();
        float d = q.lengthSquared();
        if (d == 0.0F)
            throw new ArithmeticException("Zero length quaternion.");
        float s = 2f / d;
        float xs = q.x * s, ys = q.y * s, zs = q.z * s;
        float wx = q.w * xs, wy = q.w * ys, wz = q.w * zs;
        float xx = q.x * xs, xy = q.x * ys, xz = q.x * zs;
        float yy = q.y * ys, yz = q.y * zs, zz = q.z * zs;

        float f00 = 1f - (yy + zz);
        float f01 = xy - wz;
        float f02 = xz + wy;
        float f10 = xy + wz;
        float f11 = 1f - (xx + zz);
        float f12 = yz - wx;
        float f20 = xz - wy;
        float f21 = yz + wx;
        float f22 = 1f - (xx + yy);

        // Matrix3f.mul(dest, f0-9)
        return dest.set(
                dest.m00 * f00 + dest.m01 * f10 + dest.m02 * f20,
                dest.m00 * f01 + dest.m01 * f11 + dest.m02 * f21,
                dest.m00 * f02 + dest.m01 * f12 + dest.m02 * f22,
                dest.m10 * f00 + dest.m11 * f10 + dest.m12 * f20,
                dest.m10 * f01 + dest.m11 * f11 + dest.m12 * f21,
                dest.m10 * f02 + dest.m11 * f12 + dest.m12 * f22,
                dest.m20 * f00 + dest.m21 * f10 + dest.m22 * f20,
                dest.m20 * f01 + dest.m21 * f11 + dest.m22 * f21,
                dest.m20 * f02 + dest.m21 * f12 + dest.m22 * f22
        );
    }
    
    // jesus crazy duplication...
    // dup from Quaternion::toMatrix AND Matrix4f::rotate's bottom's mul3x3
    // == Matrix4f.mul3x3(dest * Quaternion.toMatrix(q, new Matrix4f()))
    /**
     * integrate dest Matrix4f's 0-2 row/col (only change 9 element)
     */
    @SuppressWarnings("all")
    public static Matrix4f rotate(Quaternion q, Matrix4f dest) {
        // Quaternion.toMatrix(q, new Matrix4f())
        if (dest == null)
            dest = new Matrix4f();
        float d = q.lengthSquared();
        if (d == 0.0F)
            throw new ArithmeticException("Zero length quaternion.");
        float s = 2f / d;
        float xs = q.x * s, ys = q.y * s, zs = q.z * s;
        float wx = q.w * xs, wy = q.w * ys, wz = q.w * zs;
        float xx = q.x * xs, xy = q.x * ys, xz = q.x * zs;
        float yy = q.y * ys, yz = q.y * zs, zz = q.z * zs;
        
        float f00 = 1f - (yy + zz);
        float f01 = xy - wz;
        float f02 = xz + wy;
        float f10 = xy + wz;
        float f11 = 1f - (xx + zz);
        float f12 = yz - wx;
        float f20 = xz - wy;
        float f21 = yz + wx;
        float f22 = 1f - (xx + yy);

        // Matrix4f.mul3x3(dest, f0-9)
        return dest.set(
                dest.m00 * f00 + dest.m01 * f10 + dest.m02 * f20,
                dest.m00 * f01 + dest.m01 * f11 + dest.m02 * f21,
                dest.m00 * f02 + dest.m01 * f12 + dest.m02 * f22,
                dest.m03,

                dest.m10 * f00 + dest.m11 * f10 + dest.m12 * f20,
                dest.m10 * f01 + dest.m11 * f11 + dest.m12 * f21,
                dest.m10 * f02 + dest.m11 * f12 + dest.m12 * f22,
                dest.m13,

                dest.m20 * f00 + dest.m21 * f10 + dest.m22 * f20,
                dest.m20 * f01 + dest.m21 * f11 + dest.m22 * f21,
                dest.m20 * f02 + dest.m21 * f12 + dest.m22 * f22,
                dest.m23,

                dest.m30,
                dest.m31,
                dest.m32,
                dest.m33
        );
    }
}