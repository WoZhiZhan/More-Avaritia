package net.wzz.more_avaritia.client.geometry;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public final class BoltGeometry {

    /** 一条支路：折线点 + 相对粗细 + 相对亮度 */
    public record Strand(List<Vec3> points, float widthScale, float alphaScale, int depth) {}

    /** 生成参数 */
    public static class Cfg {
        /** 细分次数，每次点数翻倍。5 => 33 点，6 => 65 点。天降建议 6，技能建议 5 */
        public int detail = 6;
        /** 横向混乱程度，占全长的比例。真实闪电约 0.08~0.16 */
        public double chaos = 0.11;
        /** 每次细分后偏移的衰减系数（越小越平滑） */
        public double roughness = 0.55;
        /** 分叉概率（每个候选节点） */
        public float branchChance = 0.16f;
        /** 最大分叉层级 */
        public int maxBranchDepth = 2;
        /** 分支长度相对主干剩余长度的比例范围 */
        public double branchLenMin = 0.15, branchLenMax = 0.42;
        /** 分支相对主方向的最大偏折角（弧度） */
        public double branchAngle = 0.9;
        /** 分支整体"顺着主方向"的倾向：1=完全顺着，0=完全随机 */
        public double branchForward = 0.55;
    }

    public static List<Strand> build(Vec3 start, Vec3 end, Cfg cfg, RandomSource rand) {
        List<Strand> out = new ArrayList<>();
        buildRecursive(start, end, cfg, rand, 0, out);
        return out;
    }

    private static void buildRecursive(Vec3 a, Vec3 b, Cfg cfg, RandomSource rand, int depth, List<Strand> out) {
        int detail = Math.max(1, cfg.detail - depth);
        List<Vec3> pts = displace(a, b, detail, cfg.chaos * (1.0 + depth * 0.35), cfg.roughness, rand);

        float w = (float) Math.pow(0.52, depth);
        float al = (float) Math.pow(0.62, depth);
        out.add(new Strand(pts, w, al, depth));

        if (depth >= cfg.maxBranchDepth) return;

        Vec3 mainDir = b.subtract(a).normalize();
        // 不在头尾生成分支，看起来更自然
        for (int i = 2; i < pts.size() - 2; i++) {
            if (rand.nextFloat() >= cfg.branchChance) continue;

            Vec3 origin = pts.get(i);
            double remain = origin.distanceTo(b);
            if (remain < 1.5) continue;

            double len = remain * (cfg.branchLenMin + rand.nextDouble() * (cfg.branchLenMax - cfg.branchLenMin));
            Vec3 rnd = new Vec3(rand.nextDouble() * 2 - 1, rand.nextDouble() * 2 - 1, rand.nextDouble() * 2 - 1);
            if (rnd.lengthSqr() < 1e-6) rnd = new Vec3(0, -1, 0);
            rnd = rnd.normalize();

            Vec3 dir = mainDir.scale(cfg.branchForward)
                    .add(rnd.scale(1.0 - cfg.branchForward + rand.nextDouble() * cfg.branchAngle * 0.5));
            if (dir.lengthSqr() < 1e-6) continue;
            dir = dir.normalize();

            buildRecursive(origin, origin.add(dir.scale(len)), cfg, rand, depth + 1, out);
        }
    }

    /** 中点位移 */
    private static List<Vec3> displace(Vec3 a, Vec3 b, int iterations, double chaos, double roughness, RandomSource r) {
        List<Vec3> pts = new ArrayList<>();
        pts.add(a);
        pts.add(b);

        double offset = a.distanceTo(b) * chaos;

        for (int it = 0; it < iterations; it++) {
            List<Vec3> next = new ArrayList<>(pts.size() * 2);
            for (int i = 0; i < pts.size() - 1; i++) {
                Vec3 p = pts.get(i);
                Vec3 q = pts.get(i + 1);
                Vec3 dir = q.subtract(p);
                if (dir.lengthSqr() < 1e-8) { next.add(p); continue; }
                dir = dir.normalize();

                Vec3 n1 = perpendicular(dir);
                Vec3 n2 = dir.cross(n1).normalize();

                Vec3 mid = p.add(q).scale(0.5)
                        .add(n1.scale((r.nextDouble() * 2 - 1) * offset))
                        .add(n2.scale((r.nextDouble() * 2 - 1) * offset));

                next.add(p);
                next.add(mid);
            }
            next.add(pts.get(pts.size() - 1));
            pts = next;
            offset *= roughness;
        }
        return pts;
    }

    private static Vec3 perpendicular(Vec3 d) {
        Vec3 ref = Math.abs(d.y) > 0.95 ? new Vec3(1, 0, 0) : new Vec3(0, 1, 0);
        Vec3 p = d.cross(ref);
        return p.lengthSqr() < 1e-8 ? new Vec3(1, 0, 0) : p.normalize();
    }

    private BoltGeometry() {}
}
