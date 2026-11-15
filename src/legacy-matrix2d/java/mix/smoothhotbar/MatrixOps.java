package mix.smoothhotbar;

import net.minecraft.client.gui.DrawContext;

final class MatrixOps {
    private MatrixOps() {
    }

    static void push(DrawContext context) {
        context.getMatrices().pushMatrix();
    }

    static void pop(DrawContext context) {
        context.getMatrices().popMatrix();
    }

    static void translate(DrawContext context, float x, float y) {
        context.getMatrices().translate(x, y);
    }

    static void raise(DrawContext context) {
    }

    static void clip(DrawContext context, int x1, int y1, int x2, int y2) {
        context.enableScissor(x1, y1, x2, y2);
    }

    static void scale(DrawContext context, float x, float y) {
        context.getMatrices().scale(x, y);
    }
}
