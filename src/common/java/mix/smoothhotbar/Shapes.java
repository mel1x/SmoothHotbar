package mix.smoothhotbar;

final class Shapes {
    private static final int[][] INSETS = {{}, {1}, {2, 1}, {3, 1, 1}, {4, 2, 1, 1}};

    private Shapes() {
    }

    static int argb(int alpha, int rgb) {
        return (Math.max(0, Math.min(255, alpha)) << 24) | (rgb & 0xFFFFFF);
    }

    private static int radius(int radius, int width, int height) {
        return Math.max(0, Math.min(Math.min(radius, INSETS.length - 1), Math.min(width, height) / 2));
    }

    static void fill(HotbarView view, int x, int y, int width, int height, int radius, int color) {
        if ((color >>> 24) == 0 || width <= 0 || height <= 0) {
            return;
        }
        int r = radius(radius, width, height);
        int[] insets = INSETS[r];
        for (int row = 0; row < r; row++) {
            int inset = insets[row];
            view.fill(x + inset, y + row, x + width - inset, y + row + 1, color);
            view.fill(x + inset, y + height - 1 - row, x + width - inset, y + height - row, color);
        }
        view.fill(x, y + r, x + width, y + height - r, color);
    }

    static void ring(HotbarView view, int x, int y, int width, int height, int radius, int color) {
        if ((color >>> 24) == 0 || width <= 2 || height <= 2) {
            return;
        }
        int r = radius(radius, width, height);
        int[] outer = INSETS[r];
        int[] inner = INSETS[Math.max(0, r - 1)];

        int top = r > 0 ? outer[0] : 0;
        view.fill(x + top, y, x + width - top, y + 1, color);
        view.fill(x + top, y + height - 1, x + width - top, y + height, color);

        for (int row = 1; row < r; row++) {
            int from = outer[row];
            int to = 1 + (row - 1 < inner.length ? inner[row - 1] : 0);
            if (to <= from) {
                continue;
            }
            int upper = y + row;
            int lower = y + height - 1 - row;
            view.fill(x + from, upper, x + to, upper + 1, color);
            view.fill(x + width - to, upper, x + width - from, upper + 1, color);
            view.fill(x + from, lower, x + to, lower + 1, color);
            view.fill(x + width - to, lower, x + width - from, lower + 1, color);
        }

        int edge = Math.max(1, r);
        view.fill(x, y + edge, x + 1, y + height - edge, color);
        view.fill(x + width - 1, y + edge, x + width, y + height - edge, color);
    }
}
