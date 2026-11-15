package mix.smoothhotbar;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;

final class MatrixOps {
    private MatrixOps() {
    }

    static void push(DrawContext context) {
        context.getMatrices().push();
    }

    static void pop(DrawContext context) {
        context.getMatrices().pop();
    }

    static void translate(DrawContext context, float x, float y) {
        context.getMatrices().translate(x, y, 0f);
    }

    static void raise(DrawContext context) {
        context.getMatrices().translate(0f, 0f, 200f);
    }

    static void clip(DrawContext context, int x1, int y1, int x2, int y2) {
        MatrixStack matrices = context.getMatrices();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Vector3f from = matrix.transformPosition(x1, y1, 0f, new Vector3f());
        Vector3f to = matrix.transformPosition(x2, y2, 0f, new Vector3f());
        matrices.push();
        matrices.loadIdentity();
        context.enableScissor(Math.round(from.x), Math.round(from.y), Math.round(to.x), Math.round(to.y));
        matrices.pop();
    }

    static void scale(DrawContext context, float x, float y) {
        context.getMatrices().scale(x, y, 1f);
    }
}
