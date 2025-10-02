//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.wzz.more_avaritia.colour;

public abstract class Colour implements Copyable<Colour> {
    public byte r;
    public byte g;
    public byte b;
    public byte a;

    public Colour(int r, int g, int b, int a) {
        this.r = (byte)r;
        this.g = (byte)g;
        this.b = (byte)b;
        this.a = (byte)a;
    }

    public Colour(Colour colour) {
        this.r = colour.r;
        this.g = colour.g;
        this.b = colour.b;
        this.a = colour.a;
    }

    public abstract int pack();

    public abstract float[] packArray();

    public Colour add(Colour colour2) {
        this.a += colour2.a;
        this.r += colour2.r;
        this.g += colour2.g;
        this.b += colour2.b;
        return this;
    }

    public Colour sub(Colour colour2) {
        int ia = (this.a & 255) - (colour2.a & 255);
        int ir = (this.r & 255) - (colour2.r & 255);
        int ig = (this.g & 255) - (colour2.g & 255);
        int ib = (this.b & 255) - (colour2.b & 255);
        this.a = (byte)Math.max(ia, 0);
        this.r = (byte)Math.max(ir, 0);
        this.g = (byte)Math.max(ig, 0);
        this.b = (byte)Math.max(ib, 0);
        return this;
    }

    public Colour invert() {
        this.a = (byte)(255 - (this.a & 255));
        this.r = (byte)(255 - (this.r & 255));
        this.g = (byte)(255 - (this.g & 255));
        this.b = (byte)(255 - (this.b & 255));
        return this;
    }

    public Colour multiply(Colour colour2) {
        this.a = (byte)((int)((double)(this.a & 255) * ((double)(colour2.a & 255) / (double)255.0F)));
        this.r = (byte)((int)((double)(this.r & 255) * ((double)(colour2.r & 255) / (double)255.0F)));
        this.g = (byte)((int)((double)(this.g & 255) * ((double)(colour2.g & 255) / (double)255.0F)));
        this.b = (byte)((int)((double)(this.b & 255) * ((double)(colour2.b & 255) / (double)255.0F)));
        return this;
    }

    public Colour scale(double d) {
        this.a = (byte)((int)((double)(this.a & 255) * d));
        this.r = (byte)((int)((double)(this.r & 255) * d));
        this.g = (byte)((int)((double)(this.g & 255) * d));
        this.b = (byte)((int)((double)(this.b & 255) * d));
        return this;
    }

    public Colour interpolate(Colour colour2, double d) {
        return this.add(colour2.copy().sub(this).scale(d));
    }

    public Colour multiplyC(double d) {
        this.r = (byte)((int)MathUtils.clip((double)(this.r & 255) * d, (double)0.0F, (double)255.0F));
        this.g = (byte)((int)MathUtils.clip((double)(this.g & 255) * d, (double)0.0F, (double)255.0F));
        this.b = (byte)((int)MathUtils.clip((double)(this.b & 255) * d, (double)0.0F, (double)255.0F));
        return this;
    }

    public abstract Colour copy();

    public int rgb() {
        return (this.r & 255) << 16 | (this.g & 255) << 8 | this.b & 255;
    }

    public int argb() {
        return (this.a & 255) << 24 | (this.r & 255) << 16 | (this.g & 255) << 8 | this.b & 255;
    }

    public int rgba() {
        return (this.r & 255) << 24 | (this.g & 255) << 16 | (this.b & 255) << 8 | this.a & 255;
    }

    public abstract Colour set(int var1);

    public Colour set(Colour colour) {
        this.r = colour.r;
        this.g = colour.g;
        this.b = colour.b;
        this.a = colour.a;
        return this;
    }

    public Colour set(double r, double g, double b, double a) {
        return this.set((int)((double)255.0F * r), (int)((double)255.0F * g), (int)((double)255.0F * b), (int)((double)255.0F * a));
    }

    public Colour set(float r, float g, float b, float a) {
        return this.set((int)(255.0F * r), (int)(255.0F * g), (int)(255.0F * b), (int)(255.0F * a));
    }

    public Colour set(int r, int g, int b, int a) {
        this.r = (byte)r;
        this.g = (byte)g;
        this.b = (byte)b;
        this.a = (byte)a;
        return this;
    }

    public Colour set(double[] doubles) {
        return this.set(doubles[0], doubles[1], doubles[2], doubles[3]);
    }

    public Colour set(float[] floats) {
        return this.set(floats[0], floats[1], floats[2], floats[3]);
    }

    public Colour rF(float r) {
        this.r = (byte)((int)(255.0F * r));
        return this;
    }

    public Colour gF(float g) {
        this.g = (byte)((int)(255.0F * g));
        return this;
    }

    public Colour bF(float b) {
        this.b = (byte)((int)(255.0F * b));
        return this;
    }

    public Colour aF(float a) {
        this.a = (byte)((int)(255.0F * a));
        return this;
    }

    public Colour rF(int r) {
        this.r = (byte)r;
        return this;
    }

    public Colour gF(int g) {
        this.g = (byte)g;
        return this;
    }

    public Colour bF(int b) {
        this.b = (byte)b;
        return this;
    }

    public Colour aF(int a) {
        this.a = (byte)a;
        return this;
    }

    public float rF() {
        return (float)this.r() / 255.0F;
    }

    public float gF() {
        return (float)this.g() / 255.0F;
    }

    public float bF() {
        return (float)this.b() / 255.0F;
    }

    public float aF() {
        return (float)this.a() / 255.0F;
    }

    public int r() {
        return this.r & 255;
    }

    public int g() {
        return this.g & 255;
    }

    public int b() {
        return this.b & 255;
    }

    public int a() {
        return this.a & 255;
    }

    public static int flipABGR(int colour) {
        int a = colour >> 24 & 255;
        int b = colour >> 16 & 255;
        int c = colour >> 8 & 255;
        int d = colour & 255;
        return (d & 255) << 24 | (c & 255) << 16 | (b & 255) << 8 | a & 255;
    }

    public static int[] unpack(int colour) {
        return new int[]{colour >> 24 & 255, colour >> 16 & 255, colour >> 8 & 255, colour & 255};
    }

    public static int pack(int[] data) {
        return (data[0] & 255) << 24 | (data[1] & 255) << 16 | (data[2] & 255) << 8 | data[3] & 255;
    }

    public float[] getRGBA() {
        return new float[]{(float)this.r / 255.0F, (float)this.g / 255.0F, (float)this.b / 255.0F, (float)this.a / 255.0F};
    }

    public float[] getARGB() {
        return new float[]{(float)this.a / 255.0F, (float)this.r / 255.0F, (float)this.g / 255.0F, (float)this.b / 255.0F};
    }

    public static int packRGBA(byte r, byte g, byte b, byte a) {
        return (r & 255) << 24 | (g & 255) << 16 | (b & 255) << 8 | a & 255;
    }

    public static int packARGB(byte r, byte g, byte b, byte a) {
        return (a & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }

    public static int packRGBA(int r, int g, int b, int a) {
        return r << 24 | g << 16 | b << 8 | a;
    }

    public static int packARGB(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int packRGBA(double r, double g, double b, double a) {
        return (int)(r * (double)255.0F) << 24 | (int)(g * (double)255.0F) << 16 | (int)(b * (double)255.0F) << 8 | (int)(a * (double)255.0F);
    }

    public static int packARGB(double r, double g, double b, double a) {
        return (int)(a * (double)255.0F) << 24 | (int)(r * (double)255.0F) << 16 | (int)(g * (double)255.0F) << 8 | (int)(b * (double)255.0F);
    }

    public static int packRGBA(float[] data) {
        return packRGBA((double)data[0], (double)data[1], (double)data[2], (double)data[3]);
    }

    public static int packARGB(float[] data) {
        return packARGB((double)data[0], (double)data[1], (double)data[2], (double)data[3]);
    }

    public boolean equals(Colour colour) {
        return colour != null && this.rgba() == colour.rgba();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Colour) {
            Colour colour = (Colour)o;
            if (this.r != colour.r) {
                return false;
            } else if (this.g != colour.g) {
                return false;
            } else if (this.b != colour.b) {
                return false;
            } else {
                return this.a == colour.a;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.r;
        result = 31 * result + this.g;
        result = 31 * result + this.b;
        result = 31 * result + this.a;
        return result;
    }

    public String toString() {
        String var10000 = this.getClass().getSimpleName();
        return var10000 + "[0x" + Integer.toHexString(this.pack()).toUpperCase() + "]";
    }
}
