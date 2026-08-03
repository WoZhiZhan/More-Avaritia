package net.wzz.more_avaritia.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.wzz.more_avaritia.client.geometry.BoltGeometry;
import net.wzz.more_avaritia.client.renderer.BoltRenderer;
import net.wzz.more_avaritia.init.ModRenderTypes;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 样式 B —— 横向"技能"闪电：从施法点笔直射向目标点。
 * 与天降闪电的区别：混乱度更低（更像被约束的能量束）、有前冲的延伸过程、
 * 有缠绕主轴的螺旋副弧、命中点有冲击光斑。
 */
public class SkillBolt {

    public final Vec3 start;
    public final Vec3 end;
    public final long seed;
    public final int maxAge;
    public final float width;
    public final float r, g, b;

    public int age;
    /** 每 tick 换一次形状，缓存当帧结果 */
    private int shapeTick = -1;
    private List<BoltGeometry.Strand> shape;
    private List<Vec3> helixA, helixB;

    public SkillBolt(Vec3 start, Vec3 end, long seed, int maxAge, float width,
                     float r, float g, float b) {
        this.start = start;
        this.end = end;
        this.seed = seed;
        this.maxAge = maxAge;
        this.width = width;
        this.r = r; this.g = g; this.b = b;
    }

    /** 默认冷蓝紫技能光束，10 tick（0.5 秒） */
    public static SkillBolt of(Vec3 start, Vec3 end) {
        return new SkillBolt(start, end, RandomSource.create().nextLong(), 10, 0.13f,
                0.60f, 0.72f, 1.0f);
    }

    public boolean isDone() {
        return age >= maxAge;
    }

    public void render(Matrix4f mat, VertexConsumer vc, Vec3 camPos, float partialTick) {
        float t = age + partialTick;

        // 0~2 tick：光束向前延伸；之后全长
        float extend = Mth.clamp(t / 2.0f, 0f, 1f);
        extend = 1f - (1f - extend) * (1f - extend); // ease-out
        Vec3 tip = start.add(end.subtract(start).scale(extend));

        // 亮度：起手爆亮 -> 维持 -> 收尾衰减
        float life = t / maxAge;
        float alpha = (t < 1.5f)
                ? 0.55f + 0.45f * (t / 1.5f)
                : Mth.clamp(1.0f - (life - 0.15f) / 0.85f, 0f, 1f);
        alpha = (float) Math.pow(alpha, 0.75);
        if (alpha <= 0.01f) return;

        if (shapeTick != age) {
            shapeTick = age;
            RandomSource rand = RandomSource.create(seed + age * 31L);

            BoltGeometry.Cfg cfg = new BoltGeometry.Cfg();
            cfg.detail = 5;
            cfg.chaos = 0.045;          // 比自然闪电"直"，更像被约束的能量
            cfg.roughness = 0.60;
            cfg.branchChance = 0.13f;
            cfg.maxBranchDepth = 1;     // 技能光束分叉少而短
            cfg.branchLenMin = 0.06;
            cfg.branchLenMax = 0.18;
            cfg.branchForward = 0.35;   // 分叉更偏"横向炸开"
            cfg.branchAngle = 1.4;

            shape = BoltGeometry.build(start.subtract(camPos), tip.subtract(camPos), cfg, rand);
            helixA = helix(start.subtract(camPos), tip.subtract(camPos), 0.32, 2.6, rand.nextDouble() * Math.PI * 2, 48);
            helixB = helix(start.subtract(camPos), tip.subtract(camPos), 0.26, -3.4, rand.nextDouble() * Math.PI * 2, 48);
        } else {
            // 同一 tick 内，仍按当前 partialTick 让尖端平滑前进
            if (extend < 1f) {
                RandomSource rand = RandomSource.create(seed + age * 31L);
                BoltGeometry.Cfg cfg = new BoltGeometry.Cfg();
                cfg.detail = 5; cfg.chaos = 0.045; cfg.maxBranchDepth = 1; cfg.branchChance = 0.13f;
                shape = BoltGeometry.build(start.subtract(camPos), tip.subtract(camPos), cfg, rand);
            }
        }

        Vec3 camLocal = Vec3.ZERO; // 已经是相对相机的坐标

        for (BoltGeometry.Strand s : shape) {
            BoltRenderer.renderStrand(mat, vc, s.points(), camLocal,
                    width * s.widthScale(), r, g, b, alpha * s.alphaScale());
        }

        // 缠绕主轴的两条螺旋副弧
        if (helixA != null) {
            BoltRenderer.ribbon(mat, vc, helixA, camLocal, width * 0.45f, r, g, b, alpha * 0.55f);
            BoltRenderer.ribbon(mat, vc, helixB, camLocal, width * 0.35f, r, g, b, alpha * 0.40f);
        }

        // 施法起点的蓄能光斑 + 命中点冲击光斑
        BoltRenderer.renderFlare(mat, vc, start.subtract(camPos), camLocal,
                0.9f + 0.4f * alpha, 0.9f, 0.95f, 1.0f, alpha * 0.7f);

        if (extend >= 0.999f) {
            float hit = Mth.clamp(1.0f - (t - 2.0f) / 4.0f, 0f, 1f) * alpha;
            BoltRenderer.renderFlare(mat, vc, end.subtract(camPos), camLocal,
                    1.2f + 3.2f * hit, 0.9f, 0.95f, 1.0f, hit * 0.85f);
        }
    }

    /** 绕 start->end 轴的螺旋线 */
    private static List<Vec3> helix(Vec3 a, Vec3 b, double radius, double turns, double phase, int steps) {
        List<Vec3> pts = new ArrayList<>(steps + 1);
        Vec3 axis = b.subtract(a);
        double len = axis.length();
        if (len < 1e-4) return pts;
        Vec3 dir = axis.scale(1.0 / len);
        Vec3 ref = Math.abs(dir.y) > 0.95 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
        Vec3 n1 = dir.cross(ref).normalize();
        Vec3 n2 = dir.cross(n1).normalize();

        for (int i = 0; i <= steps; i++) {
            double f = (double) i / steps;
            double ang = phase + f * turns * Math.PI * 2;
            // 两端收束成 0 半径，看起来像被吸进光束里
            double rr = radius * Math.sin(f * Math.PI);
            pts.add(a.add(dir.scale(len * f))
                    .add(n1.scale(Math.cos(ang) * rr))
                    .add(n2.scale(Math.sin(ang) * rr)));
        }
        return pts;
    }

    public static final class Manager {
        private static final List<SkillBolt> ACTIVE = new ArrayList<>();

        /** 在客户端生成一道技能闪电（世界绝对坐标） */
        public static void spawn(SkillBolt bolt) {
            synchronized (ACTIVE) {
                if (ACTIVE.size() > 64) ACTIVE.remove(0);
                ACTIVE.add(bolt);
            }
        }

        public static void spawn(Vec3 from, Vec3 to) {
            spawn(SkillBolt.of(from, to));
        }

        public static void tick() {
            synchronized (ACTIVE) {
                Iterator<SkillBolt> it = ACTIVE.iterator();
                while (it.hasNext()) {
                    SkillBolt s = it.next();
                    s.age++;
                    if (s.isDone()) it.remove();
                }
            }
        }

        public static void clear() {
            synchronized (ACTIVE) { ACTIVE.clear(); }
        }

        public static void renderAll(PoseStack pose, Vec3 camPos, float partialTick) {
            List<SkillBolt> snapshot;
            synchronized (ACTIVE) {
                if (ACTIVE.isEmpty()) return;
                snapshot = new ArrayList<>(ACTIVE);
            }

            MultiBufferSource.BufferSource src = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vc = src.getBuffer(ModRenderTypes.BOLT);
            Matrix4f mat = pose.last().pose();

            for (SkillBolt s : snapshot) {
                s.render(mat, vc, camPos, partialTick);
            }
            src.endBatch(ModRenderTypes.BOLT);
        }

        private Manager() {}
    }
}
